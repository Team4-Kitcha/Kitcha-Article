package com.kitcha.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPickNewsDto {
    private String newsTitle;      // 기사 제목
    private String shortSummary;   // 짧은 요약
    private String longSummary;    // 상세 요약
    private String newsDate;       // 기사 날짜
    private String newsUrl;        // 기사 URL
}
