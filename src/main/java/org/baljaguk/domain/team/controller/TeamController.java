package org.baljaguk.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
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
}
