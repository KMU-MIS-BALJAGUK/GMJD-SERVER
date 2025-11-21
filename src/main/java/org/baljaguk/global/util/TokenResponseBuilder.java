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

    public ResponseEntity<ApiResponse<Void>> buildLoginResponse(JwtLoginResponse jwtLoginResponse,
                                                                HttpServletResponse response) {

        String accessToken = jwtLoginResponse.accessToken();
        String refreshToken = jwtLoginResponse.refreshToken();

        // refreshToken 쿠키 생성
        CookieUtil.addCookie(
                response,
                "refreshToken",
                refreshToken,
                cookieConfig.getDomain(),   // ".gmjd.site"
                cookieConfig.isSecure(),    // true
                cookieConfig.getSameSite()  // None
        );

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .body(ApiResponse.ok(null));
    }
}
