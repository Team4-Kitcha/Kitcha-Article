package com.kitcha.article.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NaverApiClient {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Autowired
    private ArticleCrawler articleCrawler;  // 크롤링 클래스
    @Autowired
    private GroqApiClient groqApiClient;    // Groq 요약 API

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MyPickNewsResponseDto> searchNews(String keyword) {
        try {
            String url = "https://openapi.naver.com/v1/search/news.json?query=" + keyword+ "&display=5";;

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

            // 필드 매핑
            for (JsonNode item : items) {
                String newsTitle = item.path("title").asText().replaceAll("<.*?>", "");  // HTML 태그 제거
                String newsDate = item.path("pubDate").asText();
                String newsUrl = item.path("link").asText();
                String shortSummary = item.path("description").asText().replaceAll("<.*?>", "");

                // 기사 본문 크롤링
                String articleContent = articleCrawler.getArticleContent(newsUrl);

                // Groq API 요약 (short & long)
                Map<String, String> summaries = groqApiClient.getArticleSummaries(articleContent);
                String longSummary = summaries.getOrDefault("longSummary", "요약 실패");

                // DTO 생성 및 추가
                MyPickNewsResponseDto dto = new MyPickNewsResponseDto(
                        newsTitle,
                        newsDate,
                        newsUrl,
                        shortSummary,
                        longSummary
                );
                newsList.add(dto);
            }
            return newsList;

        } catch (Exception e) {
            throw new RuntimeException("Naver API 호출 실패: " + e.getMessage(), e);
        }
    }
}
