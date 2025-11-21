package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.dto.response.MyRecruitListResponse;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamStatus;

import java.util.List;
import java.util.Map;

public interface TeamRepositoryCustom {

    Map<Long, Long> countByContestIdsGrouped(List<Long> contestIds, TeamStatus status);

    List<Team> findClosedTeamsByUserId(Long userId);

}
