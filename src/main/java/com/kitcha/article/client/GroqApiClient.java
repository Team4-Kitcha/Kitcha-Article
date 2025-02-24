package com.kitcha.article.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroqApiClient {

    @Value("${groq.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> getArticleSummaries(String content) {
        Map<String, String> summaries = new HashMap<>();
        summaries.put("longSummary", requestSummary(content, "한글로 5문장으로 요약만해서 보여줘"));
        return summaries;
    }
    // longSummary 요약 요청
    private String requestSummary(String content, String prompt) {
        try {
            String url = "https://api.groq.com/openai/v1/chat/completions";

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "당신은 뉴스 기사를 한글로 요약하는 전문가입니다."),
                    Map.of("role", "user", "content", prompt + "\n\n" + trimContent(content))
            );

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "mixtral-8x7b-32768");
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 300);
            requestBody.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.err.println("429 Too Many Requests: " + e.getMessage());
            return "요약 불가: 요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.PAYLOAD_TOO_LARGE) {
                System.err.println("413 Payload Too Large: " + e.getMessage());
                return "요약 불가: 기사 내용이 너무 큽니다.";
            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return "요약 실패: " + e.getMessage();
        }
    }
    private String trimContent(String content) {
        int maxLength = 1500;
        return (content != null && content.length() > maxLength) ? content.substring(0, maxLength) + "..." : content;
    }
    // 키워드 추출 요청
    public String extractKeyword(String content) {
        try {
            String url = "https://api.groq.com/openai/v1/chat/completions";

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "당신은 뉴스 기사에서 핵심 키워드를 한글로 하나 추출하는 전문가입니다."),
                    Map.of("role", "user", "content", "이 기사에서 가장 중요한 핵심 키워드를 한글로 하나만 알려주세요: \n\n" + content)
            );

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "mixtral-8x7b-32768");
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 10);  // 키워드만 추출하므로 짧게 설정
            requestBody.put("temperature", 0.3);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "키워드 추출 실패";
        }
    }
}
