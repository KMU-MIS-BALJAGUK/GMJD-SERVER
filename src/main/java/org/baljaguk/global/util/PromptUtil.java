package org.baljaguk.global.util;

import org.baljaguk.domain.team.dto.request.TeamApplyRequest;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;

import java.util.List;

public class PromptUtil {

    public static String generateQuestionPrompt(String endDate, String contestName) {

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

    public static String generateSummaryTagPrompt(Integer level, TeamApplyRequest request) {

        StringBuilder qaBuilder = new StringBuilder();
        for (int i = 0; i < request.answer().size(); i++) {
            qaBuilder.append("- Q").append(i + 1).append(": 질문 내용\n");
            qaBuilder.append("  A").append(i + 1).append(": ").append(request.answer().get(i)).append("\n");
        }

        return """
           당신은 공모전 팀 리더를 도와 **지원자를 빠르게 파악할 수 있는 핵심 태그를 생성하는 역할**입니다.

           아래는 지원자가 작성한 정보입니다.

           추천 레벨: %d  
           질문 및 답변 목록:
           %s

           **당신의 역할**
           - 지원자의 답변 속 핵심 성향, 강점, 관심사, 기술적 특징을 분석합니다.
           - 이 지원자를 대표할 수 있는 **요약 태그 3~4개**를 생성합니다.
           - 태그는 반드시 **한국어**로 작성합니다.
           - 태그는 가급적 **짧고 핵심적**이어야 합니다. (예: “성실함”, “협업 중심”, “문제 해결력”, “백엔드 지향”)
           - 띄어쓰기 유지, 언더스코어(_) 사용 금지.

           **응답 형식(JSON)**
           {
             "tags": [
               "태그1",
               "태그2",
               "태그3",
               "태그4"
             ]
           }

            주의사항  
           - JSON 외 다른 문장은 절대 포함하지 마십시오.  
           - 태그는 3~4개만 작성하십시오.  
           - 불필요한 설명을 넣지 마십시오.
           """.formatted(
                level,
                qaBuilder.toString()
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
