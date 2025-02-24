package com.kitcha.article.controller;

import com.kitcha.article.dto.request.InterestNewsRequestDto;
import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import com.kitcha.article.dto.response.RandomNewsResponseDto;
import com.kitcha.article.service.MyPickNewsService;
import com.kitcha.article.service.RandomNewsService;
import com.kitcha.article.service.UploadNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apps")
public class ArticleController {

    @Autowired
    private RandomNewsService randomNewsService;
    @Autowired
    private MyPickNewsService myPickNewsService;
    //@Autowired
    //private InterestService interestService;
    @Autowired
    private UploadNewsService uploadNewsService;


    // MyPick 뉴스 가챠 API
    @GetMapping("/mypick")
    public ResponseEntity<List<MyPickNewsResponseDto>> getMyPickNews(@RequestParam String keyword) {
        // 뉴스 목록 가져오기
        List<MyPickNewsResponseDto> newsList = myPickNewsService.getMyPickNews(keyword);
        //result에 List 담기
        Map<String, Object> response = new HashMap<>();
        response.put("result", newsList);
        return ResponseEntity.ok(newsList);
    }

    // 랜덤 뉴스 가챠 API
    @GetMapping("/random")
    public ResponseEntity<RandomNewsResponseDto> getRandomNews() {
        RandomNewsResponseDto randomNews = randomNewsService.getRandomNews();
        return ResponseEntity.ok(randomNews);
    }

    // 관심사 및 키워드 기반 뉴스 조회 API
    @PostMapping("/interest_news")
    public ResponseEntity<Map<String, Object>> getNewsByKeyword(@RequestBody InterestNewsRequestDto request) {
        String interest = request.getInterest();
        String keyword = request.getKeyword();

        if (interest == null || interest.isBlank() || keyword == null || keyword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "interest와 keyword가 모두 필요합니다."));
        }

        // 관심사 업데이트 서비스 호출
        //interestService.setInterest(interest);

        // 키워드 기반 뉴스 목록 조회
        List<MyPickNewsResponseDto> newsList = myPickNewsService.getMyPickNews(keyword);
        // 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("result", newsList);
        return ResponseEntity.ok(response);
    }

    // 업로드 뉴스 가챠 API
    @PostMapping("/upload")
    public ResponseEntity<List<MyPickNewsResponseDto>> uploadImageAndGetNews(@RequestParam("file") MultipartFile file) {
        // 1. 이미지 처리 및 키워드 기반 뉴스 조회
        List<MyPickNewsResponseDto> newsList = uploadNewsService.processUploadedImage(file);
        // 2. 결과 반환
        return ResponseEntity.ok(newsList);
    }
}
