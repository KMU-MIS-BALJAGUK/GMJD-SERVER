package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
}
