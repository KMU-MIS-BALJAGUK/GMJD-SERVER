package org.baljaguk.domain.contest.repository;

import org.baljaguk.domain.contest.entity.Contest;

import java.util.List;

public interface ContestRepositoryCustom {
    List<Contest> searchByKeyword(String keyword);

    List<Contest> findContestsWithFilterAndSort(
            List<String> categoryNames,
            String sortType
    );
}
