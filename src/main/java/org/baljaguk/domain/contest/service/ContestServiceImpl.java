package org.baljaguk.domain.contest.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.category.entity.Category;
import org.baljaguk.domain.category.repository.CategoryRepository;
import org.baljaguk.domain.contest.dto.request.SearchRequest;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestListResponse;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.domain.team.entity.TeamStatus;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    private final TeamRepository teamRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ContestDetailResponse getContestDetail(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_CONTEST));

        return ContestDetailResponse.from(contest);
    }

    @Override
    public ContestListResponse search(SearchRequest keywordRequest, int page, int size) {

        String keyword = keywordRequest.normalizedKeyword();

        Pageable pageable = PageRequest.of(page, size);

        // 1. 검색 결과 조회 (페이징)
        Page<Contest> contests = contestRepository.searchByKeyword(keyword, pageable);

        if (contests.isEmpty()) {
            return ContestListResponse.of(List.of(), contests.getTotalPages(), contests.getNumber(), contests.getTotalElements());
        }

        // 2. contestId 리스트
        List<Long> contestIds = contests.stream()
                .map(Contest::getId)
                .toList();

        // 3. 열린 팀 개수 조회
        Map<Long, Long> teamCountMap =
                teamRepository.countByContestIdsGrouped(contestIds, TeamStatus.OPEN);

        // 4. DTO 변환
        List<ContestListResponse.ContestSummaryResponse> summaryResponses =
                contests.stream()
                        .map(contest -> ContestListResponse.ContestSummaryResponse.of(
                                contest,
                                teamCountMap.getOrDefault(contest.getId(), 0L)
                        ))
                        .toList();

        return ContestListResponse.of(
                summaryResponses,
                contests.getNumber(),
                contests.getTotalPages(),
                contests.getTotalElements()
        );
    }

    @Override
    public ContestListResponse getContestsWithFilterAndSort(
            List<Long> categoryIdList,
            String sortType,
            int page,
            int size
    ) {

        // 1. categoryId → categoryName List로 변환
        List<String> categoryNames = null;

        if (categoryIdList != null && !categoryIdList.isEmpty()) {
            categoryNames = categoryRepository.findAllById(categoryIdList)
                    .stream()
                    .map(Category::getName)
                    .toList();
        }

        Pageable pageable = PageRequest.of(page, size);

        // 2. Page<Contest> 조회
        Page<Contest> contestsPage =
                contestRepository.findContestsWithFilterAndSort(categoryNames, sortType, pageable);

        List<Contest> contests = contestsPage.getContent();

        if (contests.isEmpty()) {
            return ContestListResponse.of(
                    List.of(),
                    page,
                    contestsPage.getTotalPages(),
                    contestsPage.getTotalElements()
            );
        }

        // 3. contestId 추출
        List<Long> contestIds = contests.stream()
                .map(Contest::getId)
                .toList();

        // 4. 팀 카운트 일괄 조회
        Map<Long, Long> teamCountMap =
                teamRepository.countByContestIdsGrouped(contestIds, TeamStatus.OPEN);

        // 5. DTO 변환
        List<ContestListResponse.ContestSummaryResponse> responses =
                contests.stream()
                        .map(contest -> {
                            long openTeams = teamCountMap.getOrDefault(contest.getId(), 0L);
                            return ContestListResponse.ContestSummaryResponse.of(contest, openTeams);
                        })
                        .toList();

        return ContestListResponse.of(
                responses,
                contestsPage.getNumber(),
                contestsPage.getTotalPages(),
                contestsPage.getTotalElements()
        );
    }
}

