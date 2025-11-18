package org.baljaguk.domain.team.dto.response;

public record TeamApplyStatusResponse(
        boolean canApply,
        String reason
) {
}
