package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.TeamApply;

import java.util.List;

public interface TeamApplyRepositoryCustom {
    Long countRequestedApplyByTeamId(Long teamId);

    List<TeamApply> findByTeamIdWithUser(Long teamId);
}
