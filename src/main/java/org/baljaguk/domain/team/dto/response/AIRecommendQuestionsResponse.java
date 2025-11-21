package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record AIRecommendQuestionsResponse(
        List<String> aiRecommendQuestionList
) {
    public static AIRecommendQuestionsResponse of(List<String> aiRecommendQuestionList) {
        return new AIRecommendQuestionsResponse(aiRecommendQuestionList);
    }
}
