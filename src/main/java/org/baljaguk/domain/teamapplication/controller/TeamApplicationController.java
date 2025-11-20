package org.baljaguk.domain.teamapplication.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.teamapplication.dto.request.TeamApplyRequest;
import org.baljaguk.domain.teamapplication.dto.response.TeamApplyResponse;
import org.baljaguk.domain.teamapplication.service.TeamApplicationService;
import org.baljaguk.global.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeamApplicationController {

    private final TeamApplicationService teamApplicationService;

    // 5) 팀 참여 신청
    @PostMapping("/teams/{teamId}/applications")
    public ApiResponse<TeamApplyResponse> applyToTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId,
            @RequestBody TeamApplyRequest request
    ) {
        TeamApplyResponse response = teamApplicationService.applyToTeam(teamId, userId, request);
        return ApiResponse.success(response);
    }

    // 나의 지원 탭에서 신청 취소
    @PostMapping("/teams/{teamId}/applications/cancel")
    public ApiResponse<Void> cancelApplication(
            @PathVariable Long teamId,
            @RequestParam Long userId
    ) {
        teamApplicationService.cancelApplication(teamId, userId);
        return ApiResponse.success(null);
    }
}
