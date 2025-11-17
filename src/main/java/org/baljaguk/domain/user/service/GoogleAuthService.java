package org.baljaguk.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.user.dto.response.JwtLoginResponse;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ApiResponse;
import org.baljaguk.global.security.client.GoogleClient;
import org.baljaguk.global.security.client.dto.GoogleAccountProfileResponse;
import org.baljaguk.global.util.JWTUtil;
import org.baljaguk.global.util.TokenResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    private final TokenResponseBuilder tokenResponseBuilder;

    public ResponseEntity<ApiResponse<Void>> loginOrRegisterWithResponse(String code) {
        log.info("🔐 [Google Login] Received code: {}", code);

        try {
            JwtLoginResponse jwtLoginResponse = loginOrRegister(code);
            return tokenResponseBuilder.buildLoginResponse(jwtLoginResponse);
        } catch (Exception e) {
            log.error("❌ [Google Login] Error during loginOrRegisterWithResponse", e);
            throw e; // 전역 에러 처리기로 전달
        }
    }

    @Transactional
    public JwtLoginResponse loginOrRegister(String code) {
        log.info("📩 [Google Login] Start login/register with code: {}", code);

        // 1. Google 유저정보 가져오기
        GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(code);
        log.info("✅ [Google Login] Retrieved profile: email={}, name={}", profile.email(), profile.name());

        // 2. DB에 유저 존재 여부 확인
        User user;
        try {
            user = userRepository.findByEmail(profile.email())
                    .orElseGet(() -> {
                        log.info("👤 [Google Login] No existing user, creating new user with email={}", profile.email());
                        return userRepository.save(
                                User.createSocialUser(
                                        profile.email(),
                                        profile.name(),
                                        profile.picture()
                                )
                        );
                    });
        } catch (Exception e) {
            log.error("❌ [Google Login] Error during user lookup or creation", e);
            throw e;
        }

        // 3. JWT 토큰 생성
        String serverAccessToken;
        String serverRefreshToken;
        try {
            serverAccessToken = jwtUtil.generateAccessToken(user.getId());
            serverRefreshToken = jwtUtil.generateRefreshToken(user.getId());
            log.info("🔑 [Google Login] Token generated: accessToken={}, refreshToken={}", serverAccessToken, serverRefreshToken);
        } catch (Exception e) {
            log.error("❌ [Google Login] Failed to generate JWT token", e);
            throw e;
        }

        return JwtLoginResponse.of(user, serverAccessToken, serverRefreshToken);
    }
}
