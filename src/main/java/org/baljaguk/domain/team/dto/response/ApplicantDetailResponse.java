package org.baljaguk.domain.team.dto.response;

import java.util.Arrays;
import java.util.List;

public record ApplicantDetailResponse(

        Long userId,
        String profileImageUrl,
        String name,
        Integer level,
        List<String> aiTags,               // CSV → List<String>
        List<String> skills,               // CSV → List<String>
        List<QuestionAnswerInfo> qaList
) {

    public static ApplicantDetailResponse of(
            Long userId,
            String profileImageUrl,
            String name,
            Integer level,
            String aiTagsCsv,          // CSV 문자열
            String skillsCsv,          // CSV 문자열
            List<QuestionAnswerInfo> qaList
    ) {
        return new ApplicantDetailResponse(
                userId,
                profileImageUrl,
                name,
                level,
                convertCsvToList(aiTagsCsv),
                convertCsvToList(skillsCsv),
                qaList
        );
    }

    /** CSV → List<String> 변환 유틸 */
    private static List<String> convertCsvToList(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public record QuestionAnswerInfo(
            String question,
            String answer
    ) {
        public static QuestionAnswerInfo of(String question, String answer) {
            return new QuestionAnswerInfo(question, answer);
        }
    }
}
