package org.baljaguk.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateTeamRequest(

        @Schema(description = "팀 제목", example = "AI 기반 공모전 추천 서비스 개발팀")
        @NotBlank(message = "팀 제목은 필수입니다.")
        String title,

        @Schema(description = "팀 최대 인원", example = "4")
        @NotNull(message = "최대 인원은 필수입니다.")
        Integer maxMember,

        @Schema(description = "팀 소개 / 모집 글",
                example = "AI 공모전에 함께 도전할 개발자, 디자이너, 기획자를 모집합니다.")
        @NotBlank(message = "팀 소개는 필수입니다.")
        String introduction,

        @Schema(description = "지원자 선별 질문 리스트",
                example = "[\"본 공모전에 지원하게 된 동기는 무엇인가요?\", \"팀 프로젝트 경험을 알려주세요.\", \"사용 가능한 기술 스택을 적어주세요.\", \"주말 투입 가능 여부를 알려주세요.\", \"협업 시 중요하게 생각하는 점은 무엇인가요?\"]")
        @NotNull(message = "질문 리스트는 필수입니다.")
        List<String> questions

) {}
