package com.kitcha.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RandomNewsDto {
    private String newsTitle;      // 기사 제목
    private String longSummary;    // 상세 요약
    private String keyword;        // 관련 키워드
}
