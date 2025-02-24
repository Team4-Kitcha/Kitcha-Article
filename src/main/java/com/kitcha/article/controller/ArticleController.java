package com.kitcha.article.controller;

import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import com.kitcha.article.dto.response.RandomNewsResponseDto;
import com.kitcha.article.service.MyPickNewsService;
import com.kitcha.article.service.RandomNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
