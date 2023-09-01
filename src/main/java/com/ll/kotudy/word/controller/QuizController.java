package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.QuizResultRequest;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import com.ll.kotudy.word.dto.response.QuizResponse;
import com.ll.kotudy.word.dto.response.QuizResultResponse;
import com.ll.kotudy.word.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;
    private final JwtProvider jwtProvider;

    @GetMapping
    public ResponseEntity<QuizResponse> create() {
        QuizResponse response = quizService.createForm();

        return ResponseEntity.ok(response);
    }

    // TODO: 퀴즈결과 반환
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuizResultResponse> result(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @RequestBody @Valid QuizResultRequest request) {

        QuizResultResponse response = quizService.applyScore(request, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return ResponseEntity.ok(response);
    }
}
