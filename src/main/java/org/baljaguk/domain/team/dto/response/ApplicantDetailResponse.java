package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record ApplicantDetailResponse(

        Long userId,
        String profileImageUrl,
        String name,
        Integer level,
        List<String> skills,              // TeamApply.skills CSV → List<String>
        List<QuestionAnswerInfo> qaList   // 질문 + 답변 리스트
) {

    public static ApplicantDetailResponse of(
            Long userId,
            String profileImageUrl,
            String name,
            Integer level,
            List<String> skills,
            List<QuestionAnswerInfo> qaList
    ) {
        return new ApplicantDetailResponse(
                userId,
                profileImageUrl,
                name,
                level,
                skills,
                qaList
        );
    }

    /**
     * 이너 DTO
     * 질문 content + 코드 작성한 answer
     */
    public record QuestionAnswerInfo(
            String question,
            String answer
    ) {
        public static QuestionAnswerInfo of(String question, String answer) {
            return new QuestionAnswerInfo(question, answer);
        }
    }
}
