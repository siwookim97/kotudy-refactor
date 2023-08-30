package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import com.ll.kotudy.word.dto.response.QuizResponse;
import com.ll.kotudy.word.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;
    private final JwtProvider jwtProvider;

    // TODO: 퀴즈 FORM 생성
    @GetMapping
    public ResponseEntity<QuizResponse> searchMyWord() {
        QuizResponse response = quizService.createForm();

        return ResponseEntity.ok(response);
    }
//
//    // TODO: 퀴즈결과 반환
//    @GetMapping
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<MyWordSearchResponse> searchMyWord(
//            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest) {
//
//        return null;
//    }
}
