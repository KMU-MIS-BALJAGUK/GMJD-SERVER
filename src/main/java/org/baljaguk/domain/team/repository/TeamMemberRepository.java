package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamMember;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    int countByTeam(Team team);

    boolean existsByTeamAndUser(Team team, User user);

    List<TeamMember> findAllByTeamOrderByIdAsc(Team team);
}
