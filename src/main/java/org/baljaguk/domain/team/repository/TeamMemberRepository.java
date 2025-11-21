package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamMember;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Long countByTeam(Team team);

    boolean existsByMemberAndTeamContest(User user, Contest contest);
}
