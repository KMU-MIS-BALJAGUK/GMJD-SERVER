package org.baljaguk.domain.contest.service;

import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestSearchResponse;

import java.util.List;

public interface ContestService {
    ContestDetailResponse getContestDetail(Long contestId);

    List<ContestSearchResponse> search(String keyword);
}
