package org.baljaguk.domain.team.service;

import org.baljaguk.domain.team.dto.request.TeamCreateRequest;
import org.baljaguk.domain.team.dto.request.TeamUpdateRequest;
import org.baljaguk.domain.team.dto.response.TeamApplyStatusResponse;
import org.baljaguk.domain.team.dto.response.TeamCreateResponse;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;

public interface TeamService {

    // 팀 신청 가능 여부 조회
    TeamApplyStatusResponse getApplyStatus(Long teamId, Long userId);

    // 팀 상세 조회
    TeamDetailResponse getTeamDetail(Long teamId, Long userId);

    // 공모전에 대한 팀 생성
    TeamCreateResponse createTeam(Long contestId, Long leaderId, TeamCreateRequest request);

    // 팀 정보 수정
    void updateTeam(Long teamId, Long leaderId, TeamUpdateRequest request);
}
