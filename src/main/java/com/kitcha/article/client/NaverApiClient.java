package com.kitcha.article.client;

import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import org.modelmapper.ModelMapper;
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

    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    public NaverApiClient(ModelMapper modelMapper) {
        this.restTemplate = new RestTemplate();
        this.modelMapper = modelMapper;
    }

    public List<MyPickNewsResponseDto> searchNews(String keyword) {
        String url = "https://openapi.naver.com/v1/search/news.json?query=" + keyword + "&display=7";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // 응답 데이터 처리
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
        List<MyPickNewsResponseDto> newsList = new ArrayList<>();

        // ModelMapper로 DTO 변환
        for (Map<String, Object> item : items) {
            MyPickNewsResponseDto dto = modelMapper.map(item, MyPickNewsResponseDto.class);
            dto.setLongSummary("긴 요약 준비 중...");  // AI 요약 전 임시 데이터
            newsList.add(dto);
        }

        return newsList;
    }
}
