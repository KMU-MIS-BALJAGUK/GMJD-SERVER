package org.baljaguk.domain.team.service;

import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;

public interface TeamService {
    AIRecommendQuestionsResponse getAIRecommendQuestions(Long contestId);
}
