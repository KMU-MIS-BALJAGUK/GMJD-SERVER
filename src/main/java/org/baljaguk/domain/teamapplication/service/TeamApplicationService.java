package org.baljaguk.domain.teamapplication.service;

import org.baljaguk.domain.teamapplication.dto.request.TeamApplyRequest;
import org.baljaguk.domain.teamapplication.dto.response.TeamApplyResponse;

public interface TeamApplicationService {

    // 5) 팀 참여 신청
    TeamApplyResponse applyToTeam(Long teamId, Long userId, TeamApplyRequest request);

    // 신청 취소
    void cancelApplication(Long teamId, Long userId);
}
