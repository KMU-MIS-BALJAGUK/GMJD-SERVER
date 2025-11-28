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
    public ContestListResponse getContestList(
            SearchRequest keywordRequest,
            List<Long> categoryIdList,
            String sortType,
            int page,
            int size
    ) {

        // 검색어 정리 (null 허용)
        String keyword = (keywordRequest != null) ? keywordRequest.normalizedKeyword() : null;

        // Category ID → Category Name 매핑
        List<String> categoryNames = null;
        if (categoryIdList != null && !categoryIdList.isEmpty()) {
            categoryNames = categoryRepository.findAllById(categoryIdList)
                    .stream()
                    .map(Category::getName)
                    .toList();
        }

        Pageable pageable = PageRequest.of(page, size);

        // Page<Contest> 조회 (검색 + 필터 + 정렬 통합)
        Page<Contest> contestPage =
                contestRepository.findContestsUnified(keyword, categoryNames, sortType, pageable);

        List<Contest> contests = contestPage.getContent();

        if (contests.isEmpty()) {
            return ContestListResponse.of(
                    List.of(),
                    page,
                    contestPage.getTotalPages(),
                    contestPage.getTotalElements()
            );
        }

        // contestId 추출
        List<Long> contestIds = contests.stream()
                .map(Contest::getId)
                .toList();

        // 열린 팀 개수 일괄 조회
        Map<Long, Long> teamCountMap =
                teamRepository.countByContestIdsGrouped(contestIds, TeamStatus.OPEN);

        // DTO 변환
        List<ContestListResponse.ContestSummaryResponse> responses =
                contests.stream()
                        .map(contest ->
                                ContestListResponse.ContestSummaryResponse.of(
                                        contest,
                                        teamCountMap.getOrDefault(contest.getId(), 0L)
                                )
                        ).toList();

        return ContestListResponse.of(
                responses,
                contestPage.getNumber(),
                contestPage.getTotalPages(),
                contestPage.getTotalElements()
        );
    }
}

