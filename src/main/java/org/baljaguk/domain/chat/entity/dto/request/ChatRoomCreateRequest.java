package org.baljaguk.domain.chat.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRoomCreateRequest(
        @NotNull(message = "팀 ID는 필수입니다.")
        @Positive(message = "팀 ID는 양수여야 합니다.")
        @Schema(description = "채팅방을 생성할 팀의 ID", example = "1")
        Long teamId
) {}