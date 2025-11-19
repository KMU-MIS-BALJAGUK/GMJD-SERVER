package org.baljaguk.domain.user.dto.request;

import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.domain.user.entity.enums.RecognizedDegree;

public record EducationUpdateRequest(
        String universityName,
        String major,
        Education education,              // 학력 상태 (Enum)
        RecognizedDegree recognizedDegree // 학위 (Enum)
) {}