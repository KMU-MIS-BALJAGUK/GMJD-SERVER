package org.baljaguk.domain.team.service;

import jakarta.validation.Valid;
import org.baljaguk.domain.team.dto.request.AIRecommendQuestionRequest;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;

public interface TeamService {
    AIRecommendQuestionsResponse getAIRecommendQuestions(@Valid AIRecommendQuestionRequest request);
}
