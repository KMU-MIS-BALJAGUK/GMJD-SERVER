package org.baljaguk.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.request.TeamCreateRequest;
import org.baljaguk.domain.team.dto.request.TeamUpdateRequest;
import org.baljaguk.domain.team.dto.response.TeamApplyStatusResponse;
import org.baljaguk.domain.team.dto.response.TeamCreateResponse;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;
import org.baljaguk.domain.team.service.TeamService;
import org.baljaguk.global.common.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeamController {

    private final TeamService teamService;

    // 2) 팀 신청 가능 여부 조회
    @GetMapping("/teams/{teamId}/apply-status")
    public ApiResponse<TeamApplyStatusResponse> getApplyStatus(
            @PathVariable Long teamId,
            @AuthenticationPrincipal Long userId
    ) {
        TeamApplyStatusResponse response = teamService.getApplyStatus(teamId, userId);
        return ApiResponse.success(response);
    }

    // 4) 팀 상세 조회
    @GetMapping("/teams/{teamId}")
    public ApiResponse<TeamDetailResponse> getTeamDetail(
            @PathVariable Long teamId,
            @AuthenticationPrincipal Long userId
    ) {
        TeamDetailResponse response = teamService.getTeamDetail(teamId, userId);
        return ApiResponse.success(response);
    }

    // 1) 공모전에 대한 팀 생성
    @PostMapping("/contests/{contestId}/teams")
    public ApiResponse<TeamCreateResponse> createTeam(
            @PathVariable Long contestId,
            @RequestBody TeamCreateRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        TeamCreateResponse response = teamService.createTeam(contestId, userId, request);
        return ApiResponse.success(response);
    }

    // 3) 팀 정보 수정
    @PatchMapping("/teams/{teamId}")
    public ApiResponse<Void> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamUpdateRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        teamService.updateTeam(teamId, userId, request);
        return ApiResponse.success(null);
    }
}
