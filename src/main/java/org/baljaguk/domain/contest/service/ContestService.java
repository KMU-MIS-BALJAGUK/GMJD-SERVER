package org.baljaguk.domain.contest.service;

import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;

public interface ContestService {
    ContestDetailResponse getContestDetail(Long contestId);
}
