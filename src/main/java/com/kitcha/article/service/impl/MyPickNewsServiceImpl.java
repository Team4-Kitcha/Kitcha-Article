package com.kitcha.article.service.impl;

import com.kitcha.article.client.NaverApiClient;
import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import com.kitcha.article.service.MyPickNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPickNewsServiceImpl implements MyPickNewsService {

    private final NaverApiClient naverApiClient;

    @Override
    public List<MyPickNewsResponseDto> getMyPickNews(String keyword) {
        // 네이버 API 호출
        return naverApiClient.searchNews(keyword);
    }
}
