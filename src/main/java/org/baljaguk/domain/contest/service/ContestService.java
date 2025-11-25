package org.baljaguk.domain.contest.service;

import jakarta.validation.Valid;
import org.baljaguk.domain.contest.dto.request.SearchRequest;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestListResponse;

import java.util.List;

public interface ContestService {
    ContestDetailResponse getContestDetail(Long contestId);

    ContestListResponse search(@Valid SearchRequest keyword, int page, int size);

    ContestListResponse getContestsWithFilterAndSort(List<Long> categoryIdList, String sortType, int page, int size);
}
