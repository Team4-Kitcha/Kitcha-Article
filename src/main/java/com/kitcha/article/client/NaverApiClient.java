package com.kitcha.article.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Component
public class NaverApiClient {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MyPickNewsResponseDto> searchNews(String keyword) {
        try {
            String url = "https://openapi.naver.com/v1/search/news.json?query=" + keyword;
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출 및 응답
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 데이터 매핑
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            List<MyPickNewsResponseDto> newsList = new ArrayList<>();

            for (JsonNode item : items) {
                MyPickNewsResponseDto dto = new MyPickNewsResponseDto(
                        item.get("title").asText(),
                        item.get("description").asText(),
                        "긴 요약 준비 중...",
                        item.get("pubDate").asText(),
                        item.get("link").asText()
                );
                newsList.add(dto);
            }

            return newsList;

        } catch (Exception e) {
            throw new RuntimeException("Naver API 호출 실패: " + e.getMessage(), e);
        }
    }
}
