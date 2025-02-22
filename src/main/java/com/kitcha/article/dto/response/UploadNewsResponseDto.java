package com.kitcha.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadNewsResponseDto {
    private String keyword;        // 관심사 키워드
    private String newsTitle;      // 기사 제목
    private String longSummary;    // 상세 요약
}
