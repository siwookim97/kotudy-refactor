package com.ll.kotudy.word.controller;

import com.ll.kotudy.word.dto.response.TodayWordResponse;
import com.ll.kotudy.word.service.TodayWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/todayWord")
public class TodayWordController {

    private final TodayWordService todayWordService;

    @GetMapping
    public ResponseEntity<TodayWordResponse> getTodayWordList() {

        TodayWordResponse response = todayWordService.getTodayWordList();

        return ResponseEntity.ok(response);
    }
}
