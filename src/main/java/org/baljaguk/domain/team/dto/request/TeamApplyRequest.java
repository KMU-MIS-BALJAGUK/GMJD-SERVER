package org.baljaguk.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "팀 신청 요청 DTO")
public record TeamApplyRequest(

        @Schema(description = "신청자가 작성한 자기소개/답변 목록",
                example = "[\"안녕하세요\", \"팀과 함께 성장하고 싶습니다!\",\"안녕하세요\", \"팀과 함께 성장하고 싶습니다!\",\"안녕하세요\"]")
        List<String> answer,

        @Schema(description = "신청자의 스킬셋 목록",
                example = "[\"Java\", \"SpringBoot\", \"React\"]")
        List<String> skills

) {}
