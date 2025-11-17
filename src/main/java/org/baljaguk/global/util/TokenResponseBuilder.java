package org.baljaguk.global.util;

import org.baljaguk.domain.user.dto.response.JwtLoginResponse;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseBuilder {

    public ResponseEntity<ApiResponse<Void>> buildLoginResponse(JwtLoginResponse jwtLoginResponse) {
        String accessToken = jwtLoginResponse.accessToken();
        String refreshToken = jwtLoginResponse.refreshToken();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .header("Set-Cookie", refreshCookie.toString())
                .body(ApiResponse.ok(null));
    }
}
