package org.baljaguk.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
import org.baljaguk.domain.team.service.TeamService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
