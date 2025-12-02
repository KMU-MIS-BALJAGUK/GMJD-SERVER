package org.baljaguk.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.dto.response.JwtLoginResponse;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.BlacklistTokenRepository;
import org.baljaguk.domain.user.repository.RefreshTokenRepository;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ApiResponse;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.baljaguk.global.api.handler.LoginException;
import org.baljaguk.global.api.handler.UserException;
import org.baljaguk.global.config.CookieConfig;
import org.baljaguk.global.config.JWTConfig;
import org.baljaguk.global.security.client.GoogleClient;
import org.baljaguk.global.security.client.dto.GoogleAccountProfileResponse;
import org.baljaguk.global.util.CookieUtil;
import org.baljaguk.global.util.JWTUtil;
import org.baljaguk.global.util.TokenResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final JWTConfig jwtConfig;
    private final CookieConfig cookieConfig;

    private final TokenResponseBuilder tokenResponseBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistTokenRepository blacklistTokenRepository;

    public ResponseEntity<ApiResponse<Void>> loginOrRegisterWithResponse(String code,
                                                                         HttpServletResponse response) {
        try {
            JwtLoginResponse jwtLoginResponse = loginOrRegister(code);
            return tokenResponseBuilder.buildTokenResponse(jwtLoginResponse.accessToken(), jwtLoginResponse.refreshToken(), response);
        }
        catch (Exception e) {
            throw new GeneralException(ErrorCode.AUTH_SOCIAL_LOGIN_FAIL);
        }
    }

    @Transactional
    public JwtLoginResponse loginOrRegister(String code) {

        // 1. Google 유저정보 가져오기
        GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(code);

        // 2. DB에 유저 존재 여부 확인
        User user;

        Optional<User> optionalUser = userRepository.findByEmail(profile.email());

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = userRepository.save(
                    User.createSocialUser(profile.email(), profile.name(), profile.picture())
            );
        }

        // 3. JWT 토큰 생성
        String serverAccessToken = jwtUtil.generateAccessToken(user.getId(), user.isRegistered());
        String serverRefreshToken = jwtUtil.generateRefreshToken(user.getId());
        log.info("🔑 JWT 발급 완료 - userId: {}, isRegistered: {}", user.getId(), user.isRegistered());

        return JwtLoginResponse.of(user, serverAccessToken, serverRefreshToken, user.isRegistered());
    }

    /**
     * 1. 회원의 식별자를 통해 서버에서 리프레시 토큰을 삭제합니다
     * 2. 액세스 토큰을 찾아서 Redis에 블랙리스트로 추가합니다
     * 2-1. 이때 남은 액세스 토큰의 만료기간을 TTL로 설정합니다
     * 3. JWTFilter 에서 블랙리스트에 해당 토큰이 있는지 탐색하고 있다면 그에 맞는 예외를 반환합니다
     * */
    public void logout(CustomUserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {
        Long id = userDetails.getUser().getId();

        // 1. Refresh Token 삭제
        refreshTokenRepository.deleteById(id);

        // 2. Access Token 추출
        String authorizationHeader = request.getHeader(jwtConfig.getHeader());

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }

        String accessToken = authorizationHeader.substring(7).trim();

        // 3. Access Token 블랙리스트 등록
        String jti = jwtUtil.getJti(accessToken);
        long expiration = jwtUtil.getRemainingExpiration(accessToken);
        blacklistTokenRepository.save(jti, expiration);

        // 4. 쿠키 삭제
        CookieUtil.deleteCookie(
                response,
                "refreshToken",
                cookieConfig.getDomain(),
                cookieConfig.isSecure(),
                cookieConfig.getSameSite()
        );
    }

    public ResponseEntity<ApiResponse<Void>> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키에서 Refresh Token 가져오기
        String refreshToken = CookieUtil.getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw new LoginException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        try {
            // 2. Refresh Token 검증 & userId 추출
            Long userId = jwtUtil.validateRefreshToken(refreshToken);

            // 3. 기존 Refresh Token의 jti 확인
            String jti = jwtUtil.getJti(refreshToken);

            // 4. 블랙리스트에 존재하면 → 재사용 공격
            if (blacklistTokenRepository.exists(jti)) {
                throw new GeneralException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            // 5. DB에서 유저 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

            // 6. 새로운 Access/Refresh Token 생성
            String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.isRegistered());
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

            // 7. 기존 Refresh Token을 블랙리스트에 저장 (남은 TTL 만큼)
            long remainingTtl = jwtUtil.getRemainingExpiration(refreshToken);
            blacklistTokenRepository.saveRefreshToken(jti, remainingTtl);

            // 8. 새 Refresh Token을 쿠키로 저장
            return tokenResponseBuilder.buildTokenResponse(
                    newAccessToken,
                    newRefreshToken,
                    response
            );
        } catch (JwtException e) {
            throw new GeneralException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
