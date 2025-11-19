package org.baljaguk.domain.contest.dto.response;

import org.baljaguk.domain.contest.entity.Contest;

public record ContestSearchResponse(
        String imageUrl,
        long remainingDays,
        long openTeamCount,
        String name,
        String organizationName
) {

    public static ContestSearchResponse of(Contest contest, long openTeamCount) {
        long remainingDays = java.time.temporal.ChronoUnit.DAYS.between(
                java.time.LocalDate.now(),
                contest.getEndDate()
        );

        return new ContestSearchResponse(
                contest.getImageUrl(),
                remainingDays,
                openTeamCount,
                contest.getName(),
                contest.getOrganizationName()
        );
    }
}
