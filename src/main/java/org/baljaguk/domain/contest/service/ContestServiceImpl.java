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
    public ContestListResponse search(SearchRequest keywordRequest) {

        String keyword = keywordRequest.normalizedKeyword();

        // 1. 검색 결과 조회
        List<Contest> contests = contestRepository.searchByKeyword(keyword);

        if (contests.isEmpty()) {
            return ContestListResponse.from(List.of());
        }

        // 2. contestId 리스트 추출
        List<Long> contestIds = contests.stream()
                .map(Contest::getId)
                .toList();

        // 3. 한 번의 쿼리로 팀 개수 조회 (N+1 제거)
        Map<Long, Long> teamCountMap =
                teamRepository.countByContestIdsGrouped(contestIds, TeamStatus.OPEN);

        // 4. DTO 변환
        List<ContestListResponse.ContestSummaryResponse> summaryResponses =
                contests.stream()
                        .map(contest -> {
                            long openTeams = teamCountMap.getOrDefault(contest.getId(), 0L);
                            return ContestListResponse.ContestSummaryResponse.of(contest, openTeams);
                        })
                        .toList();

        return ContestListResponse.from(summaryResponses);
    }

    @Override
    public ContestListResponse getContestsWithFilterAndSort(List<Long> categoryIdList, String sortType) {

        // 1. categoryId → categoryName List로 변환
        List<String> categoryNames = null;

        if (categoryIdList != null && !categoryIdList.isEmpty()) {
            categoryNames = categoryRepository.findAllById(categoryIdList)
                    .stream()
                    .map(Category::getName)
                    .toList();
        }

        // 2. QueryDSL로 필터링 + 정렬까지 DB에서 처리
        List<Contest> contests =
                contestRepository.findContestsWithFilterAndSort(categoryNames, sortType);

        if (contests.isEmpty()) {
            return ContestListResponse.from(List.of());
        }

        // 3. contestId 리스트 추출
        List<Long> contestIds = contests.stream()
                .map(Contest::getId)
                .toList();

        // 4. 팀 카운트 일괄 조회 (N+1 제거)
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

        return ContestListResponse.from(responses);
    }
}

