package org.baljaguk.domain.contest.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {
}
