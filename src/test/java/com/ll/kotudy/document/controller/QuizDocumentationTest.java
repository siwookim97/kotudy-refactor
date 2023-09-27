package com.ll.kotudy.document.controller;

import com.ll.kotudy.document.utils.DocsControllerTestBase;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.util.exception.ErrorCode;
import com.ll.kotudy.word.dto.MemberRankingDto;
import com.ll.kotudy.word.dto.QuizForm;
import com.ll.kotudy.word.dto.request.QuizResultRequest;
import com.ll.kotudy.word.dto.response.QuizResponse;
import com.ll.kotudy.word.dto.response.QuizResultResponse;
import com.ll.kotudy.word.dto.response.RankingNonMemberResponse;
import com.ll.kotudy.word.dto.response.RankingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuizDocumentationTest extends DocsControllerTestBase {

    private static final String QUIZ_CREATE_URL = "/api/v1/quiz";
    private static final String QUIZRANKING_GET_NON_AUTHORIZED_URL = "/api/v1/quiz/ranking/nonMember";
    private static final String QUIZRANKING_GET_AUTHORIZED_URL = "/api/v1/quiz/ranking";
    private static final String QUIZ_CREATE_MSG = "나만의 단어장을 기반으로 생성된 퀴즈입니다.";
    private static final String QUIZ_CREATE_NOT_ENOUGH_FAIL_MSG = "퀴즈를 만들기에 충분한 단어가 없습니다.";
    private static final String QUIZ_REFLECT_SUCCESS_MSG = "퀴즈 결과가 반영되었습니다.";
    private static final String AUTHORIZATION_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmNhQ5uHZELuMKK_1O_Wcd9lx0aggkg";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버에서 에러가 발생했습니다.";
    private static final String QUIZ_RANKING_RESULT_SUCCESS = "퀴즈 랭킹 결과입니다.";

    @Test
    @DisplayName("퀴즈 생성 DOCS")
    public void quiz_create() throws Exception {
        // given
        QuizResponse response = createQuizResponse();
        when(quizService.createForm()).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(QUIZ_CREATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("Quiz-craete-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("datum").description("10개의 4지선다 단어 퀴즈").type(JsonFieldType.ARRAY),
                                fieldWithPath("datum[].question").description("퀴즈 문제").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].answerIndex").description("퀴즈의 정답 Index (1~4)").type(JsonFieldType.NUMBER),
                                fieldWithPath("datum[].choices[]").description("퀴즈의 선택지").type(JsonFieldType.ARRAY)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("퀴즈 생성 DOCS-5xx")
    public void quiz_create_5xx() throws Exception {
        // given
        when(quizService.createForm()).thenThrow(
                new AppException(
                        ErrorCode.NOT_ENOUGH_MYWORD_FOR_CREATE_QUIZ,
                        QUIZ_CREATE_NOT_ENOUGH_FAIL_MSG)
        );

        // when
        ResultActions result = this.mockMvc.perform(
                get(QUIZ_CREATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().is5xxServerError())
                .andDo(document("Quiz-craete-5xx",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("timeStamp").description("API 요청 시간").type(JsonFieldType.STRING),
                                fieldWithPath("httpStatus").description("HTTP 상태 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("errorCode").description("HTTP 에러 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("퀴즈결과 반영 DOCS")
    public void quiz_update() throws Exception {
        // given
        QuizResultRequest request = createQuizResultRequest();
        QuizResultResponse response = createQuizResultReseponse();
        when(quizService.applyScore(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                patch(QUIZ_CREATE_URL)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("Quiz-patch-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        requestFields(
                                fieldWithPath("score").description("퀴즈 결과 점수").type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("ranking").description("점수가 적용된 회원의 랭킹").type(JsonFieldType.NUMBER),
                                fieldWithPath("score").description("회원의 최종 점수").type(JsonFieldType.NUMBER)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("퀴즈결과 반영 DOCS-5xx-SCORE-NOT-NUMBER")
    public void quiz_update_5xx_score_not_number() throws Exception {
        // given
        when(quizService.applyScore(any(), any())).thenThrow(new RuntimeException(INTERNAL_SERVER_ERROR_MESSAGE));

        // when
        ResultActions result = this.mockMvc.perform(
                patch(QUIZ_CREATE_URL)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .content("{\n" +
                                "    \"score\": \"asd\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is5xxServerError())
                .andDo(document("Quiz-patch-5xx",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        responseFields(
                                fieldWithPath("timeStamp").description("API 요청 시간").type(JsonFieldType.STRING),
                                fieldWithPath("httpStatus").description("HTTP 상태 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("errorCode").description("HTTP 에러 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("퀴즈결과 반영 DOCS-5xx-EMPTY-BODY")
    public void quiz_update_5xx_empty_body() throws Exception {
        // given
        when(quizService.applyScore(any(), any())).thenThrow(new RuntimeException(INTERNAL_SERVER_ERROR_MESSAGE));

        // when
        ResultActions result = this.mockMvc.perform(
                patch(QUIZ_CREATE_URL)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is5xxServerError())
                .andDo(document("Quiz-patch-5xx",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        responseFields(
                                fieldWithPath("timeStamp").description("API 요청 시간").type(JsonFieldType.STRING),
                                fieldWithPath("httpStatus").description("HTTP 상태 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("errorCode").description("HTTP 에러 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("비 로그인 퀴즈 랭킹 출력 DOCS")
    public void quizRanking_non_member_get() throws Exception {
        // given
        RankingNonMemberResponse response = createRankingNonMemberResponse();
        when(quizService.getNonMemberQuizRanking()).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(QUIZRANKING_GET_NON_AUTHORIZED_URL)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("QuizRankingNonMember-get-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("topMemberRanking").description("퀴즈 점수 상위 랭커 10명의 정보 (같은 점수라면 가입 순으로 정렬)").type(JsonFieldType.ARRAY),
                                fieldWithPath("topMemberRanking[].ranking").description("랭커의 순위").type(JsonFieldType.NUMBER),
                                fieldWithPath("topMemberRanking[].username").description("랭커의 이름 ").type(JsonFieldType.STRING),
                                fieldWithPath("topMemberRanking[].score").description("랭커의 점수").type(JsonFieldType.NUMBER)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 퀴즈 랭킹 출력 DOCS")
    public void quizRanking_get() throws Exception {
        // given
        RankingResponse response = createRankingResponse();
        when(quizService.getQuizRaking(any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(QUIZRANKING_GET_AUTHORIZED_URL)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("QuizRanking-get-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("userRanking").description("로그인한 회원의 랭킹").type(JsonFieldType.NUMBER),
                                fieldWithPath("username").description("로그인한 회원의 이름").type(JsonFieldType.STRING),
                                fieldWithPath("userScore").description("로그인한 회원의 점수 ").type(JsonFieldType.NUMBER),
                                fieldWithPath("topMemberRanking").description("퀴즈 점수 상위 랭커 10명의 정보 (같은 점수라면 가입 순으로 정렬)").type(JsonFieldType.ARRAY),
                                fieldWithPath("topMemberRanking[].ranking").description("랭커의 순위").type(JsonFieldType.NUMBER),
                                fieldWithPath("topMemberRanking[].username").description("랭커의 이름 ").type(JsonFieldType.STRING),
                                fieldWithPath("topMemberRanking[].score").description("랭커의 점수").type(JsonFieldType.NUMBER)
                        )))
                .andDo(print());
    }

    private QuizResponse createQuizResponse() {
        List<QuizForm> datum = new ArrayList<>();

        for (int i = 1; i < 11; i++) {
            List<String> choices = new ArrayList<>();
            choices.add("단어" + (i * 4));
            choices.add("단어" + (i * 4 + 1));
            choices.add("단어" + (i * 4 + 2));
            choices.add("단어" + (i * 4 + 3));
            QuizForm form = new QuizForm("의미" + (i * 4), 1, choices);
            datum.add(form);
        }

        return new QuizResponse(QUIZ_CREATE_MSG, datum);
    }

    private QuizResultRequest createQuizResultRequest() {
        return new QuizResultRequest(5);
    }

    private QuizResultResponse createQuizResultReseponse() {
        return new QuizResultResponse(QUIZ_REFLECT_SUCCESS_MSG, 1, 5);
    }

    private RankingNonMemberResponse createRankingNonMemberResponse() {
        List<MemberRankingDto> datum = new ArrayList<>();
        addMemberRankingDto(datum);

        return new RankingNonMemberResponse(QUIZ_RANKING_RESULT_SUCCESS, datum);
    }

    private RankingResponse createRankingResponse() {
        List<MemberRankingDto> datum = new ArrayList<>();
        addMemberRankingDto(datum);

        return new RankingResponse(QUIZ_RANKING_RESULT_SUCCESS, 1, "1번 사람", 9, datum);
    }

    private void addMemberRankingDto(List<MemberRankingDto> datum) {
        for (int i = 1; i < 5; i++) {
            MemberRankingDto dto = new MemberRankingDto(i + "번 사람", (10 - i));
            datum.add(dto);
        }
        datum.forEach(dto -> dto.setRanking(datum.indexOf(dto) + 1));
    }
}
