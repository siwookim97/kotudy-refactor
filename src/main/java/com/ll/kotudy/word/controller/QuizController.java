package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.QuizResultRequest;
import com.ll.kotudy.word.dto.response.RankingResponse;
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
    public ResponseEntity<QuizResponse> getQuiz() {
        QuizResponse response = quizService.createForm();

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuizResultResponse> applyQuizResult(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @RequestBody @Valid QuizResultRequest request) {

        QuizResultResponse response = quizService.applyScore(request, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RankingResponse> getQuizRanking(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest) {

        RankingResponse response = quizService.getQuizRaking(jwtProvider.getId(tokenHeaderRequest.getToken()));

        return ResponseEntity.ok(response);
    }
}
