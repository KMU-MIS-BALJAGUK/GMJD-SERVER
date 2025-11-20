package org.baljaguk.domain.contest.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom {
    List<Contest> findAll();
}
