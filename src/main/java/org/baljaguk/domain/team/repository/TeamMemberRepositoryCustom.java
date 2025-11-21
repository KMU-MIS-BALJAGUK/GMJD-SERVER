package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.TeamMember;

import java.util.List;

public interface TeamMemberRepositoryCustom {
    List<TeamMember> findAllWithUserByTeamId(Long teamId);
}
