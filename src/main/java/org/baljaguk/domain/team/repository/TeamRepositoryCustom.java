package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.TeamStatus;

import java.util.List;
import java.util.Map;

public interface TeamRepositoryCustom {

    Map<Long, Long> countByContestIdsGrouped(List<Long> contestIds, TeamStatus status);
}
