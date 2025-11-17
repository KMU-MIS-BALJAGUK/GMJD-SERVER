package org.baljaguk.domain.user.dto.response;

public record UserRes(
        Long userId,
        String name,
        String email
) {
}
