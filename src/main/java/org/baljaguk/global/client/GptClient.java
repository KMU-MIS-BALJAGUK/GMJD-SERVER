package org.baljaguk.global.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.global.config.GptConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptClient {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;  // GptConfig 에서 만든 Bean
    private final GptConfig gptConfig;

    private static final String GPT_URL = "https://api.openai.com/v1/chat/completions";

    public String callOpenAI(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", gptConfig.getModel(),
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    GPT_URL,
                    HttpMethod.POST,
                    entity,
                    (Class<Map<String, Object>>)(Class<?>)Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("GPT 호출 실패 - status: {}, body: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("GPT API가 비정상 상태 코드를 반환했습니다.");
            }


            Map<String, Object> body = response.getBody();
            if (body == null) {
                throw new RuntimeException("GPT 응답이 비어있습니다.");
            }

            Object choicesObj = body.get("choices");
            if (!(choicesObj instanceof List)) {
                log.error("GPT 응답에 choices 필드가 없거나 형식이 잘못되었습니다: {}", body);
                throw new RuntimeException("GPT 응답 형식이 올바르지 않습니다(choices).");
            }

            List<?> choices = (List<?>) choicesObj;
            if (choices.isEmpty()) {
                throw new RuntimeException("GPT 응답의 choices가 비어 있습니다.");
            }

            Object firstChoice = choices.get(0);
            if (!(firstChoice instanceof Map)) {
                throw new RuntimeException("GPT 응답 형식이 올바르지 않습니다(first choice).");
            }

            Map<?, ?> firstChoiceMap = (Map<?, ?>) firstChoice;
            Object messageObj = firstChoiceMap.get("message");
            if (!(messageObj instanceof Map)) {
                throw new RuntimeException("GPT 응답 형식이 올바르지 않습니다(message).");
            }

            Map<?, ?> messageMap = (Map<?, ?>) messageObj;
            Object contentObj = messageMap.get("content");
            if (!(contentObj instanceof String)) {
                throw new RuntimeException("GPT 응답 형식이 올바르지 않습니다(content).");
            }

            return (String) contentObj;

        } catch (Exception e) {
            log.error("🔥 GPT 호출 오류: {}", e.getMessage(), e);
            throw new RuntimeException("GPT API 요청 중 오류 발생", e);
        }
    }
}
