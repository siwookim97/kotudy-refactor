package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.QuizResultRequest;
import com.ll.kotudy.word.dto.response.RankingNonMemberResponse;
import com.ll.kotudy.word.dto.response.RankingResponse;
import com.ll.kotudy.word.dto.response.QuizResponse;
import com.ll.kotudy.word.dto.response.QuizResultResponse;
import com.ll.kotudy.word.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;
    private final JwtProvider jwtProvider;

    @GetMapping
    public EntityModel<QuizResponse> createQuiz() {
        QuizResponse response = quizService.createForm();

        return toModelQuizResponse(response);
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    public EntityModel<QuizResultResponse> applyQuizResult(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @RequestBody @Valid QuizResultRequest request) {

        QuizResultResponse response = quizService.applyScore(request, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return toModelQuizResultResponse(tokenHeaderRequest, request, response);
    }

    @GetMapping("/ranking/nonMember")
    public EntityModel<RankingNonMemberResponse> getQuizRanking_nonMember() {

        RankingNonMemberResponse response = quizService.getNonMemberQuizRanking();

        return toModelRankingNonMemberResponse(response);
    }

    @GetMapping("/ranking")
    @PreAuthorize("isAuthenticated()")
    public EntityModel<RankingResponse> getQuizRanking(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest) {

        RankingResponse response = quizService.getQuizRaking(jwtProvider.getId(tokenHeaderRequest.getToken()));

        return toModelRankingResponse(tokenHeaderRequest, response);
    }

    private EntityModel<QuizResponse> toModelQuizResponse(QuizResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(QuizController.class)
                        .createQuiz())
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(QuizController.class)
                        .applyQuizResult(new TokenHeaderRequest("Authorization Token"), new QuizResultRequest(10)))
                        .withRel("apply-quiz-result")
                        .withType("PATCH")
        );
    }

    private EntityModel<QuizResultResponse> toModelQuizResultResponse(
            TokenHeaderRequest tokenHeaderRequest, QuizResultRequest request, QuizResultResponse response) {

        return EntityModel.of(response,
                linkTo(methodOn(QuizController.class)
                        .applyQuizResult(tokenHeaderRequest, request))
                        .withSelfRel()
                        .withType("PATCH"),
                linkTo(methodOn(QuizController.class)
                        .getQuizRanking(tokenHeaderRequest))
                        .withRel("get-quiz-ranking")
                        .withType("GET")
        );
    }

    private EntityModel<RankingNonMemberResponse> toModelRankingNonMemberResponse(RankingNonMemberResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(QuizController.class)
                        .getQuizRanking_nonMember())
                        .withSelfRel()
                        .withType("GET")
        );
    }

    private EntityModel<RankingResponse> toModelRankingResponse(
            TokenHeaderRequest tokenHeaderRequest, RankingResponse response) {

        return EntityModel.of(response,
                linkTo(methodOn(QuizController.class)
                        .getQuizRanking(tokenHeaderRequest))
                        .withSelfRel()
                        .withType("GET")
        );
    }
}
