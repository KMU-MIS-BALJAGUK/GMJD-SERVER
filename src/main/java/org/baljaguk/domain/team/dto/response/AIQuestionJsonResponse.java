package org.baljaguk.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIQuestionJsonResponse(
        @JsonProperty("questionList")
        List<String> questionList
) {}
