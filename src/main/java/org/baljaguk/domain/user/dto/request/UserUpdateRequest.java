package org.baljaguk.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.domain.user.entity.enums.RecognizedDegree;

import java.util.List;

public record UserUpdateRequest(

        @Schema(description = "한줄 소개", example = "안녕하세요! 열정 넘치는 백엔드 개발자입니다.")
        String introduction,

        @Schema(description = "대학교 이름", example = "고려대학교")
        String universityName,

        @Schema(description = "전공명", example = "컴퓨터공학과")
        String major,

        @ArraySchema(schema = @Schema(description = "스킬셋", example = "Java"), arraySchema = @Schema(description = "스킬셋 목록", example = "[\"Java\", \"Spring Boot\", \"AWS\"]"))
        List<String> skills,

        @ArraySchema(schema = @Schema(description = "관심 분야", example = "AI"), arraySchema = @Schema(description = "관심 분야 목록", example = "[\"백엔드\", \"AI\", \"ML\"]"))
        List<String> interests,

        @Schema(description = "학력", example = "UNIVERSITY")
        Education education,

        @Schema(description = "인정학력", example = "BACHELOR")
        RecognizedDegree recognizedDegree

) {}
