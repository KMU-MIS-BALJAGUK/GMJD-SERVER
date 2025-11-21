package org.baljaguk.domain.contest.service;

import org.baljaguk.domain.contest.dto.request.SearchRequest;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestListResponse;

import java.util.List;

public interface ContestService {
    ContestDetailResponse getContestDetail(Long contestId);

    ContestListResponse search(SearchRequest keyword);

    ContestListResponse getContestsWithFilterAndSort(List<Long> categoryIdList, String sortType);
}
