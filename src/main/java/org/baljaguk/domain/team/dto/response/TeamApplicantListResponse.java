package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record TeamApplicantListResponse(
        Long teamId,
        List<ApplicantInfo> applicants
) {

    public static TeamApplicantListResponse of(Long teamId, List<ApplicantInfo> applicants) {
        return new TeamApplicantListResponse(teamId, applicants);
    }

    // 지원자 1명 정보
    public record ApplicantInfo(
            String profileImageUrl,
            String name
    ) {
        public static ApplicantInfo of(String profileImageUrl, String name) {
            return new ApplicantInfo(profileImageUrl, name);
        }
    }
}
