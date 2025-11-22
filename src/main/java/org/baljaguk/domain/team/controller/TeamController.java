package org.baljaguk.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.request.TeamApplyRequest;
import org.baljaguk.domain.team.dto.response.*;
import org.baljaguk.domain.team.service.TeamService;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/{contestId}/ai-question")
    public ResponseEntity<ApiResponse<AIRecommendQuestionsResponse>> getAIRecommendQuestions(
            @PathVariable Long contestId
    ) {
        AIRecommendQuestionsResponse response = teamService.getAIRecommendQuestions(contestId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/{contestId}")
    public ResponseEntity<ApiResponse<Void>> createTeam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long contestId,
            @RequestBody @Valid CreateTeamRequest request
    ) {
        Long userId = userDetails.getUserId();

        teamService.createTeam(userId, contestId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<ApiResponse<ContestTeamListResponse>> getTeamList(@PathVariable Long contestId) {
        ContestTeamListResponse response = teamService.getTeamList(contestId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{teamId}/detail")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeamDetail(
            @PathVariable Long teamId
    ) {
        TeamDetailResponse response = teamService.getTeamDetail(teamId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/apply/{teamId}")
    public ResponseEntity<ApiResponse<Void>> applyTeam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody TeamApplyRequest request
    ) {
        teamService.applyTeam(userDetails.getUserId(), teamId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/my-teams")
    public ResponseEntity<ApiResponse<MyTeamListResponse>> getMyTeamList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        MyTeamListResponse response = teamService.getMyTeamList(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/my-recruit")
    public ResponseEntity<ApiResponse<MyRecruitListResponse>> getMyRecruitList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyRecruitListResponse response = teamService.getMyRecruitList(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/my-applies")
    public ResponseEntity<ApiResponse<MyApplyListResponse>> getMyApplyList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyApplyListResponse response = teamService.getMyApplyList(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/my-teams/{teamId}")
    public ResponseEntity<ApiResponse<MyTeamDetailResponse>> getMyTeamDetail(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyTeamDetailResponse response = teamService.getMyTeamDetail(userDetails.getUserId(), teamId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/my-recruit/{teamId}")
    public ResponseEntity<ApiResponse<TeamApplicantListResponse>> getTeamApplicants(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TeamApplicantListResponse response = teamService.getTeamApplicants(teamId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
