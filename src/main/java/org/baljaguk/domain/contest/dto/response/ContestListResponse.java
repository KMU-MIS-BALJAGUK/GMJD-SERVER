package org.baljaguk.domain.contest.dto.response;

import org.baljaguk.domain.contest.entity.Contest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public record ContestListResponse(
        List<ContestSummaryResponse> contests
) {

    public static ContestListResponse from(List<ContestSummaryResponse> contests) {
        return new ContestListResponse(contests);
    }

    public static record ContestSummaryResponse(
            String imageUrl,
            long remainingDays,
            long openTeamCount,
            String name,
            String organizationName
    ) {

        public static ContestSummaryResponse of(Contest contest, long openTeamCount) {
            long remainingDays = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    contest.getEndDate()
            );

            return new ContestSummaryResponse(
                    contest.getImageUrl(),
                    remainingDays,
                    openTeamCount,
                    contest.getName(),
                    contest.getOrganizationName()
            );
        }
    }
}
