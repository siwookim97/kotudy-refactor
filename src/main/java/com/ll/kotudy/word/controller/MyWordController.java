package com.ll.kotudy.word.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/myword")
public class MyWordController {

    @PostMapping
    public ResponseEntity<String> addMyWord() {
        return ResponseEntity.ok().body("나만의 단어장 추가 완료.");
    }
}
