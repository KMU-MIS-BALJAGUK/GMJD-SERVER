package org.baljaguk.domain.contest.service;

import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestListResponse;

public interface ContestService {
    ContestDetailResponse getContestDetail(Long contestId);

    ContestListResponse search(String keyword);

    ContestListResponse getContestsWithFilterAndSort(Long categoryId, String sortType);
}
