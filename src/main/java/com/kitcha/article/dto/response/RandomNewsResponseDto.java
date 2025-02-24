package com.kitcha.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RandomNewsResponseDto {
    private String newsTitle;
    private String longSummary;    // 상세 요약
    private String interest;       // 관심사
    private String keyword;        // 핵심 키워드 (검색용)
}
