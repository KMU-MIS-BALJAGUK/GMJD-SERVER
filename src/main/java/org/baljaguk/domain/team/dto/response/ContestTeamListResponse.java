package org.baljaguk.domain.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.baljaguk.domain.team.dto.response.enums.CanApply;

import java.util.List;

@Schema(description = "공모전별 팀 목록 응답")
public record ContestTeamListResponse(

        @Schema(description = "팀 목록")
        List<TeamInfo> teams

) {
    public static ContestTeamListResponse of(List<TeamInfo> teams) {
        return new ContestTeamListResponse(teams);
    }

    // Inner DTO
    @Schema(description = "팀 정보 DTO")
    public record TeamInfo(

            @Schema(description = "팀 ID", example = "12")
            Long teamId,

            @Schema(description = "팀 제목", example = "AI 기반 공모전 팀")
            String title,

            @Schema(description = "최대 인원수", example = "5")
            Integer maxMember,

            @Schema(description = "현재 팀 참여 인원수", example = "3")
            Long currentMemberCount,

            @Schema(description = "팀 상태 (OPEN만 조회됨)", example = "OPEN")
            String status,

            @Schema(description = "신청 가능 여부", implementation = CanApply.class)
            CanApply canApply
    ) {
        public static TeamInfo of(Long teamId,
                                  String title,
                                  Integer maxMember,
                                  Long currentMemberCount,
                                  String status,
                                  CanApply canApply) {

            return new TeamInfo(teamId, title, maxMember, currentMemberCount, status, canApply);
        }
    }
}
