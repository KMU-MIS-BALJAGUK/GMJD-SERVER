package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamStatus;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    List<Team> findByContestAndStatus(Contest contest, TeamStatus status);

    boolean existsByContestAndTeamLeaderAndStatus(
            Contest contest,
            User teamLeader,
            TeamStatus status
    );
}
