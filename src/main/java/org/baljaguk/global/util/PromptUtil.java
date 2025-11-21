package org.baljaguk.global.util;

import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;

public class PromptUtil {

    public static String generatePrompt(String endDate, String contestName) {

        return """
                저는 팀의 리더가 되어 %s에 마감되는 %s 이라는 제목의 공모전에 참여할 팀을 모집하려고 합니다.
                제가 모집 중인 팀에 신청한 지원자를 선별할 때 하기 좋은 가볍고 쉬운 질문 5개를 아래의 json 형태로 응답해주세요.

                출력 형식(JSON):
                {
                    "questionList": [
                        "질문1",
                        "질문2",
                        "질문3",
                        "질문4",
                        "질문5"
                    ]
                }
                """.formatted(
                endDate,
                contestName
        );
    }

    public static String extractJson(String response) {

        int startIdx = response.indexOf('{');
        int endIdx = response.lastIndexOf('}');

        if (startIdx == -1 || endIdx == -1 || endIdx <= startIdx) {
            throw new GeneralException(ErrorCode.GPT_RESPONSE_PARSE_ERROR);
        }

        return response.substring(startIdx, endIdx + 1);
    }
}
