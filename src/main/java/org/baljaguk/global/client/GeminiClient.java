package org.baljaguk.global.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.global.config.GeminiConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestTemplate restTemplate;
    private final GeminiConfig geminiConfig;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    public String generate(String prompt) {

        String url = GEMINI_URL.formatted(
                geminiConfig.getModel(),
                geminiConfig.getApiKey()
        );

        log.info("📌 Gemini 요청 URL = {}", url);

        // Gemini requestBody 규격에 맞게 변환
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("📥 Gemini 응답: {}", response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("❌ Gemini 호출 실패 - status: {}, body: {}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("Gemini API가 비정상 상태 코드를 반환했습니다.");
            }

            return extractText(response.getBody());

        } catch (Exception e) {
            log.error("🔥 Gemini 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Gemini API 요청 중 오류 발생", e);
        }
    }

    /** Gemini 응답 text 추출 */
    private String extractText(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            log.error("❌ Gemini 응답 파싱 실패: {}", e.getMessage());
            throw new RuntimeException("Gemini 응답 파싱 오류", e);
        }
    }
}
