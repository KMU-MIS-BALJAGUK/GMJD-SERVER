package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record MyTeamListResponse(
        List<MyTeamInfoResponse> teams
) {
    public static MyTeamListResponse of(List<MyTeamInfoResponse> teams) {
        return new MyTeamListResponse(teams);
    }

    public static record MyTeamInfoResponse(
            Long teamId,
            String contestImageUrl,
            String contestName,
            String contestOrganizationName,
            Integer maxMember,
            Long memberCount,
            String type
    ) {
        public static MyTeamInfoResponse of(
                Long teamId,
                String contestImageUrl,
                String contestName,
                String contestOrganizationName,
                Integer maxMember,
                Long memberCount,
                String type
        ) {
            return new MyTeamInfoResponse(
                    teamId,
                    contestImageUrl,
                    contestName,
                    contestOrganizationName,
                    maxMember,
                    memberCount,
                    type
            );
        }
    }
}
