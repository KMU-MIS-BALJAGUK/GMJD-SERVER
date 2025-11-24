package org.baljaguk.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자의 보유 스킬 수정 요청 DTO")
public record SkillUpdateRequest(

        @Schema(
                description = "사용자가 보유한 스킬 리스트",
                example = "[\"Java\", \"Spring Boot\", \"AWS\", \"Docker\"]"
        )
        List<String> skills
) {}
