package org.baljaguk.domain.user.dto.response;

import org.baljaguk.domain.user.entity.User;

public record JwtLoginResponse(
        String accessToken,
        String refreshToken,
        String name,
        String profileImageUrl
) {

    public static JwtLoginResponse of(User user, String accessToken, String refreshToken) {
        return new JwtLoginResponse(
                accessToken,
                refreshToken,
                user.getName(),
                user.getProfileImageUrl()
        );
    }
}
