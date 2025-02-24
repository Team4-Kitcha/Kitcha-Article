package com.kitcha.article.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.kitcha.article.client.ArticleCrawler;
import com.kitcha.article.client.GroqApiClient;
import com.kitcha.article.client.NaverApiClient;
import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import com.kitcha.article.service.MyPickNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyPickNewsServiceImpl implements MyPickNewsService {

    @Autowired
    private NaverApiClient naverApiClient;
    @Autowired
    private ArticleCrawler articleCrawler;
    @Autowired
    private GroqApiClient groqApiClient;

    @Override
    public List<MyPickNewsResponseDto> getMyPickNews(String interest) {
        JsonNode items = naverApiClient.fetchNews(interest,5);
        List<MyPickNewsResponseDto> newsList = new ArrayList<>();

        // 필드 매핑
        for (JsonNode item : items) {
            String newsTitle = item.path("title").asText().replaceAll("<.*?>", "");  // HTML 태그 제거
            String newsDate = item.path("pubDate").asText();
            String newsUrl = item.path("link").asText();
            String shortSummary = item.path("description").asText().replaceAll("<.*?>", "");

            // 기사 본문 크롤링
            String articleContent = articleCrawler.getArticleContent(newsUrl);

            // Groq API 요약
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
    }
}
