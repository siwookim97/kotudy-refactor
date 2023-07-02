package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.service.MyWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/myword")
public class MyWordController {

    private final MyWordService myWordService;
    private final JwtProvider jwtProvider;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addMyWord(@RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest) {

        System.out.println("token = " + tokenHeaderRequest.getToken());
        System.out.println(jwtProvider.getId(tokenHeaderRequest.getToken()));
        System.out.println(jwtProvider.getUsername(tokenHeaderRequest.getToken()));

        return ResponseEntity.ok().body("님의 나만의 단어장에 단어추가 완료.");
    }
}
