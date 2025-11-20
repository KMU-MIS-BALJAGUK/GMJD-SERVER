package org.baljaguk.domain.teamapplication.repository;

import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.teamapplication.entity.TeamApplication;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {

    boolean existsByTeamAndUser(Team team, User user);

    int countByTeam(Team team);

    List<TeamApplication> findAllByTeam(Team team);
}
