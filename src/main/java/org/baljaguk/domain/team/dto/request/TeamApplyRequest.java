package org.baljaguk.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "팀 신청 요청 DTO")
public record TeamApplyRequest(

        @NotNull(message = "답변 목록(answer)은 null일 수 없습니다.")
        @Schema(description = "신청자가 작성한 자기소개/답변 목록",
                example = "[\"안녕하세요\", \"팀과 함께 성장하고 싶습니다!\",\"안녕하세요\", \"팀과 함께 성장하고 싶습니다!\",\"안녕하세요\"]")
        List<@NotBlank(message = "답변 내용은 비어 있을 수 없습니다.") String> answer,

        @NotNull(message = "스킬셋(skills)은 null일 수 없습니다.")
        @Schema(description = "신청자의 스킬셋 목록",
                example = "[\"Java\", \"SpringBoot\", \"React\"]")
        List<@NotBlank(message = "스킬 항목은 비어 있을 수 없습니다.") String> skills

) {}
