package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("""
        SELECT COUNT(t) FROM Team t
        WHERE t.contest.id = :contestId
          AND t.status = :status
    """)
    long countByContestIdAndStatus(Long contestId, TeamStatus status);
}
