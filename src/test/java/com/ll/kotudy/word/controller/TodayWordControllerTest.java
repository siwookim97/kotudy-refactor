package com.ll.kotudy.word.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.domain.TodayWord;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.service.MyWordService;
import com.ll.kotudy.word.service.TodayWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class TodayWordControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MyWordService myWordService;

    @Autowired
    private TodayWordService todayWordService;

    final private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUpMockMvcForRestDocs(WebApplicationContext webApplicationContext,
                                 RestDocumentationContextProvider restDocumentationContextProvider) throws ServletException {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    public void todayWord_get() throws Exception {
        // given
        String response = "오늘의 단어 목록입니다.";
        Long id = memberService.join("홍길동", "qwer1234").getId();

        // when
        for (int i = 0; i < 10; i++) {
            myWordService.add(new MyWordAddRequest("name" + i, "morpheme" + i, "mean" + i), id);
        }
        todayWordService.setupTodayWordList();

        // then
        mockMvc.perform(get("/api/v1/todayWord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("TodayWord-get",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("datum").description("나만의 단어장을 기반으로 생성된 오늘의 단어 (24시, 자정 초기화)").type(JsonFieldType.ARRAY),
                                fieldWithPath("datum[].name").description("오늘의 단어").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].morpheme").description("오늘의 단어 품사").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].mean").description("오늘의 단어 의미").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }
}