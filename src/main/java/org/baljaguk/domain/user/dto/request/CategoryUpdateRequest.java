package org.baljaguk.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "사용자의 관심 카테고리 수정 요청 DTO")
public record CategoryUpdateRequest(

        @Schema(
                description = "변경할 카테고리 ID 목록",
                example = "[1, 3, 5]"
        )
        Set<Long> categoryIds
) {}
