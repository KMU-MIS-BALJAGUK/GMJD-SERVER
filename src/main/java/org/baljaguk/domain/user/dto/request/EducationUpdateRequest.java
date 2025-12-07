package org.baljaguk.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.domain.user.entity.enums.RecognizedDegree;

@Schema(description = "사용자의 학력 정보 수정 요청 DTO")
public record EducationUpdateRequest(

        @Schema(
                description = "대학교 이름",
                example = "Kookmin University"
        )
        String universityName,

        @Schema(
                description = "전공명",
                example = "Management Information Systems"
        )
        String major,

        @Schema(
                description = "HIGH_SCHOOL, UNIVERSITY, MASTER",
                example = "UNIVERSITY"
        )
        Education education,

        @Schema(
                description = "ASSOCIATE, BACHELOR",
                example = "ASSOCIATE"
        )
        RecognizedDegree recognizedDegree
) {}
