package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByContestAndLeader(Contest contest, User leader);
}
