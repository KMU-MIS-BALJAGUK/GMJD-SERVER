package org.baljaguk.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.dto.response.JwtLoginResponse;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.BlacklistTokenRepository;
import org.baljaguk.domain.user.repository.RefreshTokenRepository;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ApiResponse;
import org.baljaguk.global.config.CookieConfig;
import org.baljaguk.global.config.JWTConfig;
import org.baljaguk.global.security.client.GoogleClient;
import org.baljaguk.global.security.client.dto.GoogleAccountProfileResponse;
import org.baljaguk.global.util.CookieUtil;
import org.baljaguk.global.util.JWTUtil;
import org.baljaguk.global.util.TokenResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseEntity<ApiResponse<Void>> loginOrRegisterWithResponse(String code) {
            JwtLoginResponse jwtLoginResponse = loginOrRegister(code);
            return tokenResponseBuilder.buildLoginResponse(jwtLoginResponse);
    }

    @Transactional
    public JwtLoginResponse loginOrRegister(String code) {

        // 1. Google 유저정보 가져오기
        GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(code);

        // 2. DB에 유저 존재 여부 확인
        User user;
        user = userRepository.findByEmail(profile.email())
                .orElseGet(() -> {
                    return userRepository.save(
                            User.createSocialUser(
                                    profile.email(),
                                    profile.name(),
                                    profile.picture()
                            )
                    );
                });

        // 3. JWT 토큰 생성
        String serverAccessToken;
        String serverRefreshToken;
        serverAccessToken = jwtUtil.generateAccessToken(user.getId());
        serverRefreshToken = jwtUtil.generateRefreshToken(user.getId());

        return JwtLoginResponse.of(user, serverAccessToken, serverRefreshToken);
    }

    /**
     * @param userDetails userDetails 에서 회원의 id를 받아서 그걸로 리프레시 토큰을 삭제합니다
     * @param request 헤더를 블랙리스트에 추가하기 위해 필요합니다
     *
     *
     * 로그아웃 로직은 다음과 같습니다
     * 1. 회원의 식별자를 통해 서버에서 리프레시 토큰을 삭제합니다
     * 2. 액세스 토큰을 찾아서 블랙리스트에 추가합니다
     * 2-1. 이때 남은 액세스 토큰의 만료기간을 TTL로 설정합니다
     * 3. JWTFilter 에서 블랙리스트에 해당 토큰이 있는지 탐색하고 있다면 그에 맞는 예외를 반환합니다
     * */
    public void logout(CustomUserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {

        Long id = userDetails.getUser().getId();
        refreshTokenRepository.deleteById(id);

        String authorizationHeader = request.getHeader(jwtConfig.getHeader());

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }

        String accessToken = authorizationHeader.substring(7).trim();

        String jti = jwtUtil.getJti(accessToken);
        long expiration = jwtUtil.getRemainingExpiration(accessToken);

        blacklistTokenRepository.save(jti, expiration);

        CookieUtil.deleteCookie(
                response,
                "refresh-token",
                cookieConfig.getDomain(),
                cookieConfig.isSecure(),
                cookieConfig.getSameSite()
        );
    }
}
