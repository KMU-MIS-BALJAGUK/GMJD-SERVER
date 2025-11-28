package org.baljaguk.domain.contest.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContestRepositoryCustom {
    Page<Contest> findContestsUnified(String keyword, List<String> categoryNames, String sortType, Pageable pageable);
}
