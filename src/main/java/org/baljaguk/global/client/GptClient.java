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
            ResponseEntity<Map> response = restTemplate.exchange(
                    GPT_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getBody() == null) {
                throw new RuntimeException("GPT 응답이 비어있습니다.");
            }

            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) response.getBody().get("choices");

            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");

        } catch (Exception e) {
            log.error("🔥 GPT 호출 오류: {}", e.getMessage(), e);
            throw new RuntimeException("GPT API 요청 중 오류 발생", e);
        }
    }
}
