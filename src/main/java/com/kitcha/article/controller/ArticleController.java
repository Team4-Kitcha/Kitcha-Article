package com.kitcha.article.controller;

import com.kitcha.article.dto.response.MyPickNewsResponseDto;
import com.kitcha.article.service.MyPickNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apps")
public class ArticleController {

    private final MyPickNewsService myPickNewsService;

    public ArticleController(MyPickNewsService myPickNewsService) {
        this.myPickNewsService = myPickNewsService;
    }
    // 관심사 뉴스 API
    @GetMapping("/mypick")
    public ResponseEntity<List<MyPickNewsResponseDto>> getMyPickNews(@RequestParam String keyword) {
        // 뉴스 목록 가져오기
        List<MyPickNewsResponseDto> newsList = myPickNewsService.getMyPickNews(keyword);
        //result에 List 담기
        Map<String, Object> response = new HashMap<>();
        response.put("result", newsList);
        return ResponseEntity.ok(newsList);
    }
}
