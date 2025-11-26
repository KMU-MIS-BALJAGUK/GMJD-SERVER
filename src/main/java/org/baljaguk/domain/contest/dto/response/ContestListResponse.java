package org.baljaguk.domain.contest.dto.response;

import org.baljaguk.domain.contest.entity.Contest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public record ContestListResponse(
        List<ContestSummaryResponse> contests,
        int currentPage,      // 현재 페이지 번호
        int totalPages,       // 전체 페이지 개수
        long totalElements    // 전체 공모전 개수
) {

    public static ContestListResponse of(
            List<ContestSummaryResponse> contests,
            int currentPage,
            int totalPages,
            long totalElements
    ) {
        return new ContestListResponse(contests, currentPage, totalPages, totalElements);
    }

    public static record ContestSummaryResponse(
            Long id,
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
                    contest.getId(),
                    contest.getImageUrl(),
                    remainingDays,
                    openTeamCount,
                    contest.getName(),
                    contest.getOrganizationName()
            );
        }
    }
}
