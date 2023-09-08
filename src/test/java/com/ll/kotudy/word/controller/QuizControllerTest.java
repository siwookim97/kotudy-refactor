package com.ll.kotudy.word.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.request.QuizResultRequest;
import com.ll.kotudy.word.service.MyWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class QuizControllerTest {

    private MockMvc mockMvc;
    private static String token;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MyWordRepository myWordRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MyWordService myWordService;

    final private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUpMockMvcForRestDocs(WebApplicationContext webApplicationContext,
                                 RestDocumentationContextProvider restDocumentationContextProvider) throws ServletException {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();

        memberRepository.deleteAll();
        myWordRepository.deleteAll();
    }

    @Test
    public void quiz_create() throws Exception {
        // given
        String response = "나만의 단어장을 기반으로 생성된 퀴즈입니다.";
        Long id = memberService.join("홍길동", "qwer1234").getId();

        // when
        for (int i = 0; i < 40; i++) {
            myWordService.add(new MyWordAddRequest("name" + i, "morpheme" + i, "mean" + i), id);
        }

        // then
        mockMvc.perform(get("/api/v1/quiz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("Quiz-craete-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
    @WithMockUser(username = "김길동", roles = {"USER"})
    public void quiz_update() throws Exception {
        // given
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("김길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("김길동", "qwer1234");
        QuizResultRequest quizResultRequest = new QuizResultRequest(5);
        String response = "퀴즈 결과가 반영되었습니다.";
        Integer id = 0;

        // when
        MvcResult mvcResultJoin = mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andReturn();

        MvcResult mvcResultLogin = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        id = JsonPath.parse(mvcResultJoin.getResponse().getContentAsString()).read("$.id");
        token = JsonPath.parse(mvcResultLogin.getResponse().getContentAsString()).read("$.accessToken");

        for (int i = 0; i < 40; i++) {
            myWordService.add(new MyWordAddRequest("name" + i, "morpheme" + i, "mean" + i), id.longValue());
        }

        // then
        mockMvc.perform(patch("/api/v1/quiz")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(objectMapper.writeValueAsString(quizResultRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("Quiz-patch-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
    public void quizRankingNonMember_get() throws Exception {
        // given
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");
        String response = "퀴즈 랭킹 결과입니다.";

        // when
        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andReturn();

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        // then
        mockMvc.perform(get("/api/v1/quiz/ranking/nonMember")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("QuizRankingNonMember-get-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
    @WithMockUser(username = "이길동", roles = {"USER"})
    public void quizRanking_get() throws Exception {
        // given
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("이길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("이길동", "qwer1234");
        QuizResultRequest quizResultRequest = new QuizResultRequest(5);
        String response = "퀴즈 랭킹 결과입니다.";

        // when
        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andReturn();

        MvcResult mvcResultLogin = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        token = JsonPath.parse(mvcResultLogin.getResponse().getContentAsString()).read("$.accessToken");

        // then
        mockMvc.perform(get("/api/v1/quiz/ranking")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("QuizRanking-get-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
}