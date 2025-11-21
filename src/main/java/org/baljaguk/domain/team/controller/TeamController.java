package org.baljaguk.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.request.AIRecommendQuestionRequest;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
import org.baljaguk.domain.team.service.TeamService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/ai-question")
    public ResponseEntity<ApiResponse<AIRecommendQuestionsResponse>> getAIRecommendQuestions(
            @RequestBody @Valid AIRecommendQuestionRequest request
    ) {
        AIRecommendQuestionsResponse response = teamService.getAIRecommendQuestions(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
