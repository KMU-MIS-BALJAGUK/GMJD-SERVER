package org.baljaguk.domain.contest.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;

    @Override
    public ContestDetailResponse getContestDetail(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_CONTEST));

        return ContestDetailResponse.from(contest);
    }
}

