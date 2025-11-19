package org.baljaguk.domain.contest.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.category.entity.Category;
import org.baljaguk.domain.category.repository.CategoryRepository;
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
    public ContestListResponse search(String keyword) {

        List<Contest> contests = contestRepository.searchByKeyword(keyword);

        List<ContestListResponse.ContestSummaryResponse> summaryResponses =
                contests.stream()
                        .map(contest -> {
                            long openTeams = teamRepository.countByContestIdAndStatus(
                                    contest.getId(),
                                    TeamStatus.OPEN
                            );

                            return ContestListResponse.ContestSummaryResponse.of(
                                    contest,
                                    openTeams
                            );
                        })
                        .toList();

        return ContestListResponse.from(summaryResponses);
    }

    @Override
    public ContestListResponse getContestsWithFilterAndSort(List<Long> categoryIdList, String sortType) {

        // 1. 전체 contest 조회
        List<Contest> contests = contestRepository.findAll();

        // 2. categoryId 리스트 필터링 처리
        if (categoryIdList != null && !categoryIdList.isEmpty()) {

            // categoryId -> categoryName 리스트로 변환
            List<String> categoryNames = categoryRepository.findAllById(categoryIdList)
                    .stream()
                    .map(Category::getName)
                    .toList();

            // 필터링: Contest.categories 에 categoryNames 중 하나라도 포함되면 통과
            contests = contests.stream()
                    .filter(contest -> {
                        List<String> contestCategoryList = List.of(contest.getCategories().split(","));
                        return contestCategoryList.stream()
                                .anyMatch(categoryNames::contains);
                    })
                    .toList();
        }

        // 3. 정렬
        contests = switch (sortType) {
            // 최신순
            case "latest" -> contests.stream()
                    .sorted(Comparator.comparing(Contest::getStartDate).reversed())
                    .toList();

            // 인기순
            case "popular" -> contests.stream()
                    .sorted(Comparator.comparing(Contest::getViews).reversed())
                    .toList();

            // 마감임박순
            case "deadline" -> contests.stream()
                    .sorted(Comparator.comparing(Contest::getEndDate))
                    .toList();

            // 디폴트는 최신순
            default -> contests.stream()
                    .sorted(Comparator.comparing(Contest::getStartDate).reversed())
                    .toList();
        };

        // 4. DTO 변환
        List<ContestListResponse.ContestSummaryResponse> responses = contests.stream()
                .map(contest -> {
                    long openTeams = teamRepository.countByContestIdAndStatus(
                            contest.getId(),
                            TeamStatus.OPEN
                    );
                    return ContestListResponse.ContestSummaryResponse.of(contest, openTeams);
                })
                .toList();

        // 5. 최종 Response 감싸서 반환
        return ContestListResponse.from(responses);
    }
}

