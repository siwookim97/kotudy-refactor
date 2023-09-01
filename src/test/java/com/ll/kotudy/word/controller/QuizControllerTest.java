package com.ll.kotudy.word.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.service.MyWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class QuizControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MyWordRepository myWordRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MyWordService myWordService;

    @Autowired
    private JwtProvider jwtProvider;

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

    @BeforeEach
    void setUpMyWord() {

    }

    @Test
    public void quiz_create() throws Exception {
        // given
        Long id = memberService.join("홍길동", "qwer1234").getId();
        for (int i = 0; i < 40; i++) {
            myWordService.add(new MyWordAddRequest("name" + i, "morpheme" + i, "mean" + i), id);
        }

        String response = "나만의 단어장을 기반으로 생성된 퀴즈입니다.";

        mockMvc.perform(get("/api/v1/quiz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("Quiz-craete",
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
}