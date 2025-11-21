package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.team.entity.RegisterStatus;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamApply;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamApplyRepository extends JpaRepository<TeamApply, Long> {
    boolean existsByUserAndTeam(User user, Team team);

    boolean existsByUserAndTeamContestAndStatus(User user, Contest contest, RegisterStatus status);
}
