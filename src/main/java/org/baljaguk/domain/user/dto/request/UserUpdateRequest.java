package org.baljaguk.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.domain.user.entity.enums.RecognizedDegree;

import java.util.List;

public record UserUpdateRequest(

        @NotBlank(message = "한줄 소개는 필수입니다")
        @Schema(description = "한줄 소개", example = "안녕하세요! 열정 넘치는 백엔드 개발자입니다.")
        String introduction,

        @NotNull(message = "학력은 필수입니다")
        @Schema(description = "학력", example = "UNIVERSITY")
        Education education,

        @NotBlank(message = "학교 이름은 필수입니다")
        @Schema(description = "학교 이름", example = "고려대학교")
        String universityName,

        @Schema(description = "인정학력", example = "BACHELOR")
        RecognizedDegree recognizedDegree,

        @Schema(description = "전공명", example = "컴퓨터공학과")
        String major,

        @ArraySchema(schema = @Schema(description = "관심 분야 카테고리 ID", example = "1"), arraySchema = @Schema(description = "관심 분야 ID 목록", example = "[1, 2, 3]"))
        List<Long> categoryIds, // 선택 필드 (nullable, optional)

        @ArraySchema(schema = @Schema(description = "스킬셋", example = "Java"), arraySchema = @Schema(description = "스킬셋 목록", example = "[\"Java\", \"Spring Boot\", \"AWS\"]"))
        List<String> skills // 선택 필드 (nullable, optional)

) {}
