package org.baljaguk.global.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.dto.response.JwtLoginResponse;
import org.baljaguk.global.api.ApiResponse;
import org.baljaguk.global.config.CookieConfig;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenResponseBuilder {

    private final CookieConfig cookieConfig;

    public ResponseEntity<ApiResponse<Void>> buildTokenResponse(
            String accessToken,
            String refreshToken,
            HttpServletResponse response
    ) {
        // refreshToken 쿠키 재설정
        CookieUtil.addCookie(
                response,
                "refreshToken",
                refreshToken,
                cookieConfig.getDomain(),
                cookieConfig.isSecure(),
                cookieConfig.getSameSite()
        );

        // AccessToken 헤더 전달
        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
