package org.baljaguk.domain.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "팀 상세 조회 응답 DTO")
public record TeamDetailResponse(

        @Schema(description = "팀 제목", example = "AI 공모전 같이 하실 분!")
        String title,

        @Schema(description = "팀장 이름", example = "김현정")
        String leaderName,

        @Schema(description = "팀 생성일자 (yyyy.MM.dd)", example = "2025.06.30")
        String createdAt,

        @Schema(description = "현재 팀원 수", example = "3")
        Long memberCount,

        @Schema(description = "최대 팀원 수", example = "5")
        Integer maxMember,

        @Schema(description = "공모전 마감일 (yyyy.MM.dd)", example = "2025.06.30")
        String contestEndDate,

        @Schema(description = "팀 소개", example = "AI 기반의 프로젝트를 함께 진행할 팀원을 찾습니다.")
        String introduction,

        @Schema(description = "질문 리스트")
        List<String> questionList

) {
    public static TeamDetailResponse of(String title,
                                        String leaderName,
                                        String createdAt,
                                        Long memberCount,
                                        Integer maxMember,
                                        String contestEndDate,
                                        String introduction,
                                        List<String> questionList) {

        return new TeamDetailResponse(
                title,
                leaderName,
                createdAt,
                memberCount,
                maxMember,
                contestEndDate,
                introduction,
                questionList
        );
    }
}
