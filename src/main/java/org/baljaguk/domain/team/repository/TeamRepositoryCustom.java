package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.TeamStatus;

public interface TeamRepositoryCustom {
    long countByContestIdAndStatus(Long contestId, TeamStatus status);
}
