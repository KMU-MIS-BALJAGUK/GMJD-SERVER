package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record MyRecruitListResponse(
        List<MyRecruitInfoResponse> recruitList
) {
    public static MyRecruitListResponse of(List<MyRecruitInfoResponse> recruitList) {
        return new MyRecruitListResponse(recruitList);
    }

    public record MyRecruitInfoResponse(
            Long teamId,
            String contestImageUrl,
            String contestName,
            String contestOrganizationName,
            Integer maxMember,
            Long memberCount,
            Long requestedCount,    // TeamApply 중 REQUESTED 상태 개수
            String status           // team.status.displayName
    ) {
        public static MyRecruitInfoResponse of(
                Long teamId,
                String contestImageUrl,
                String contestName,
                String contestOrganizationName,
                Integer maxMember,
                Long memberCount,
                Long requestedCount,
                String status
        ) {
            return new MyRecruitInfoResponse(
                    teamId,
                    contestImageUrl,
                    contestName,
                    contestOrganizationName,
                    maxMember,
                    memberCount,
                    requestedCount,
                    status
            );
        }
    }
}
