package org.baljaguk.domain.team.service;

import org.baljaguk.domain.team.dto.response.TeamApplyStatusResponse;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;

public interface TeamService {

    /** 팀 신청 가능 여부 조회 */
    TeamApplyStatusResponse getApplyStatus(Long teamId, Long userId);

    /** 팀 상세 조회 */
    TeamDetailResponse getTeamDetail(Long teamId);

}
