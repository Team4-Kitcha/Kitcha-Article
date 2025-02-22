package com.kitcha.article.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterestNewsRequestDto {
    private String interest;    // 관심 키워드
}
