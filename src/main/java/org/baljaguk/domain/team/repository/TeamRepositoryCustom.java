package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.dto.ContestInfoDto;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeamRepositoryCustom {

    Map<Long, Long> countByContestIdsGrouped(List<Long> contestIds, TeamStatus status);

    List<Team> findClosedTeamsByUserId(Long userId);

    Optional<Team> findTeamWithContestByTeamId(Long teamId);

    List<ContestInfoDto> findContestInfoByTeamIds(List<Long> teamIds);

}
