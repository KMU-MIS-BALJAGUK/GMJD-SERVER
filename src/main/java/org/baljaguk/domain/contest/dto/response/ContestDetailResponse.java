package org.baljaguk.domain.contest.dto.response;

import org.baljaguk.domain.contest.entity.Contest;

public record ContestDetailResponse(
        String name,
        String organizationName,
        String companyType,
        String benefits,
        String awardScale,
        String duration,                // "startDate ~ endDate" 형태
        String targetParticipants,
        String siteUrl,
        String additionalBenefits,
        String categories,
        String imageUrl
) {
    public static ContestDetailResponse from(Contest contest) {
        return new ContestDetailResponse(
                contest.getName(),
                contest.getOrganizationName(),
                contest.getCompanyType(),
                contest.getBenefits(),
                contest.getAwardScale(),
                contest.getStartDate() + " ~ " + contest.getEndDate(),
                contest.getTargetParticipants(),
                contest.getSiteUrl(),
                contest.getAdditionalBenefits(),
                contest.getCategories(),
                contest.getImageUrl()
        );
    }
}
