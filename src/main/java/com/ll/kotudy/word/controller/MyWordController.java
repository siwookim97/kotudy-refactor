package com.ll.kotudy.word.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/myword")
public class MyWordController {

    @PostMapping
    public ResponseEntity<String> addMyWord(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName() + "님의 나만의 단어장에 단어추가 완료.");
    }
}
