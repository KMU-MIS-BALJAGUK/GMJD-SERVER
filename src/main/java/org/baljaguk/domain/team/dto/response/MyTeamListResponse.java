package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record MyTeamListResponse(
        List<MyTeamInfoResponse> teams
) {
    public static MyTeamListResponse of(List<MyTeamInfoResponse> teams) {
        return new MyTeamListResponse(teams);
    }

    public static record MyTeamInfoResponse(
            String contestImageUrl,
            String contestName,
            String contestOrganizationName,
            Long memberCount,
            String type
    ) {
        public static MyTeamInfoResponse of(
                String contestImageUrl,
                String contestName,
                String contestOrganizationName,
                Long memberCount,
                String type
        ) {
            return new MyTeamInfoResponse(
                    contestImageUrl,
                    contestName,
                    contestOrganizationName,
                    memberCount,
                    type
            );
        }
    }
}
