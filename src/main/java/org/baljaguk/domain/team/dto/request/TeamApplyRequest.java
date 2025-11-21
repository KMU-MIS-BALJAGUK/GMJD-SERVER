package org.baljaguk.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 신청 요청 DTO")
public record TeamApplyRequest(

        @Schema(description = "신청자가 작성한 자기소개/답변", example = "팀과 함께 성장하고 싶습니다!")
        String answer,

        @Schema(description = "신청자의 스킬셋 (,로 구분)", example = "Java,SpringBoot,React")
        String skills

) {}
