package org.baljaguk.domain.contest.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
}

