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

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    private final TokenResponseBuilder tokenResponseBuilder;

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
}
