package com.ll.kotudy.document.controller;

import com.ll.kotudy.document.utils.DocsControllerTestBase;
import com.ll.kotudy.word.controller.TodayWordController;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.response.TodayWordResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodayWordDocumentationTest extends DocsControllerTestBase {

    private static final String TODAYWORD_GET = "/api/v1/todayWord";
    private static final String TODAYWORD_GET_MSG = "오늘의 단어 목록입니다.";

    @Test
    @DisplayName("오늘의 단어 조회 DOCS")
    public void todayWord_get() throws Exception {
        // given
        TodayWordResponse response = createTodayWordResponse();
        when(todayWordService.getTodayWordList()).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(TODAYWORD_GET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("TodayWord-get-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("datum").description("나만의 단어장을 기반으로 생성된 오늘의 단어 (24시, 자정 초기화)").type(JsonFieldType.ARRAY),
                                fieldWithPath("datum[].name").description("오늘의 단어").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].morpheme").description("오늘의 단어 품사").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].mean").description("오늘의 단어 의미").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    private TodayWordResponse createTodayWordResponse() {
        List<TodayWordDto> datum = new ArrayList<>();
        TodayWordDto data1 = new TodayWordDto("1번 단어", "1번 품사", "1번 의미");
        TodayWordDto data2 = new TodayWordDto("2번 단어", "2번 품사", "2번 의미");
        datum.add(data1);
        datum.add(data2);

        return new TodayWordResponse(TODAYWORD_GET_MSG, datum);
    }
}