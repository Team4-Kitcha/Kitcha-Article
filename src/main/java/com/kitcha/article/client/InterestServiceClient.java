package com.kitcha.article.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class InterestServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVER_API_URL = "http://gateway-server:8072/authentication/users/interest";


    public void setInterest(String interest, HttpHeaders headers) {
        // 헤더에서 사용자 ID와 JWT 토큰 가져오기
        if (!headers.containsKey("Authorization")) {
            throw new IllegalArgumentException("헤더에 Authorization이 누락되었습니다.");
        }
        String jwtToken = headers.getFirst("Authorization");  // JWT 토큰


        // 관심사만 전달
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("interest", interest);

        // 3. 헤더 설정 (jwt 토큰 등록)
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", jwtToken);

        // 4. HTTP 요청 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);


        // 디버깅 로그
        System.out.println("🚀 [API 요청] 관심사 전달 시작");
        System.out.println("🌐 요청 URL: " + USER_SERVER_API_URL);
        System.out.println("🔐 JWT Token: " + jwtToken);
        System.out.println("📦 요청 본문: " + requestBody);

        // 5. 외부 API 호출
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(USER_SERVER_API_URL, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("관심사 등록 성공: " + response.getBody());
            } else {
                System.out.println("관심사 등록 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
        }
    }
}
