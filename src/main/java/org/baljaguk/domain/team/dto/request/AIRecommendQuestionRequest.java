package org.baljaguk.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AIRecommendQuestionRequest(
        @NotNull(message = "contestId는 필수입니다.")
        Long contestId,

        @NotBlank(message = "teamTitle은 필수입니다.")
        String teamTitle,

        @NotBlank(message = "teamIntroduction은 필수입니다.")
        String teamIntroduction
) {
}
