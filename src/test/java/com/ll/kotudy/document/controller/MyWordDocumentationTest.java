package com.ll.kotudy.document.controller;

import com.ll.kotudy.document.utils.DocsControllerTestBase;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.util.exception.ErrorCode;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordAddResponse;
import com.ll.kotudy.word.dto.response.MyWordDeleteResponse;
import com.ll.kotudy.word.dto.response.MyWordResponse;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyWordDocumentationTest extends DocsControllerTestBase {

    private static final String MYWORD_URL = "/api/v1/myWord";
    private static final String AUTHORIZATION_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmNhQ5uHZELuMKK_1O_Wcd9lx0aggkg";
    private static final String MYWORD_ADD_MSG = "이름 단어 추가가 성공하였습니다.";
    private static final String MYWORD_ADD_ALREADY_EXIST_MSG = " 단어 추가가 실패하였습니다.(이미 단어장에 존재)";
    private static final String REQUEST_HEADER_FORMAT_MSG = "올바른 요청 Header 형식이 아닙니다.";
    private static final String MYWORD_DELETE_SUCCESS_MSG = "번의 단어 삭제를 성공하였습니다.";
    private static final String MYWORD_DEELETE_DO_NOT_EXIST_MSG = "번의 단어 삭제가 실패하였습니다. (나만의 단어장에 존재하지 않는 단어입니다)";
    private static final String MYWROD_SEARCH_SUCCESS = "나만의 단어 검색 결과는 다음과 같습니다.";

    @Test
    @DisplayName("나만의 단어장에 단어 추가 DOCS")
    void myWord_add() throws Exception {
        // given
        MyWordAddRequest request = createMyWordAddRequest();
        MyWordAddResponse response = createMyWordAddResponse(request.getName() + MYWORD_ADD_MSG);
        when(myWordService.add(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                post(MYWORD_URL)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("MyWord-add-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        requestFields(
                                fieldWithPath("name").description("저장을 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("morpheme").description("저장을 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("저장을 요청한 단어의 정보").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.wordId").description("저장을 요청한 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.name").description("저장을 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("data.morpheme").description("저장을 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("data.mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.delete-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.delete-myWord.href").description("Delete-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.delete-myWord.type").description("Delete-MyWord Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.search-myWord.href").description("Search-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord.type").description("Search-MyWord Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("나만의 단어장에 단어 추가 DOCS-200-ALREADY-EXIST")
    void myWord_add_fail_already_exist() throws Exception {
        // given
        MyWordAddRequest request = createMyWordAddRequest();
        MyWordAddResponse response = createMyWordAddResponse(MYWORD_ADD_ALREADY_EXIST_MSG);
        when(myWordService.add(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                post(MYWORD_URL)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("MyWord-add-200-already-exist",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        requestFields(
                                fieldWithPath("name").description("저장을 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("morpheme").description("저장을 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("저장을 요청한 단어의 정보").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.wordId").description("저장을 요청한 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.name").description("저장을 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("data.morpheme").description("저장을 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("data.mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.delete-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.delete-myWord.href").description("Delete-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.delete-myWord.type").description("Delete-MyWord Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.search-myWord.href").description("Search-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord.type").description("Search-MyWord Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("나만의 단어장에 단어 추가 DOCS-4xx-LOGOUTED")
    void myWord_add_4xx() throws Exception {
        // given
        MyWordAddRequest request = createMyWordAddRequest();
        when(myWordService.add(any(), any())).thenThrow(
                new AppException(
                        ErrorCode.HEADER_BAD_REQUEST,
                        REQUEST_HEADER_FORMAT_MSG)
        );

        // when
        ResultActions result = this.mockMvc.perform(
                post(MYWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().is4xxClientError())
                .andDo(document("MyWord-add-4xx-logouted",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").description("저장을 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("morpheme").description("저장을 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("timeStamp").description("API 요청 시간").type(JsonFieldType.STRING),
                                fieldWithPath("httpStatus").description("HTTP 상태 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("errorCode").description("HTTP 에러 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("나만의 단어장에 단어 삭제 DOCS")
    void myWord_delete() throws Exception {
        // given
        MyWordDeleteResponse response = createMyWordDeleteResponse();
        when(myWordService.delete(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                delete(MYWORD_URL + "/{myWordId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("MyWord-delete-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("myWordId").description("삭제 요청할 단어 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("삭제를 요청한 단어의 정보").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.wordId").description("삭제를 요청한 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.name").description("삭제를 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("data.morpheme").description("삭제를 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("data.mean").description("삭제를 요청한 단어의 의미").type(JsonFieldType.STRING).ignored(),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.search-myWord.href").description("Search-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord.type").description("Search-MyWord Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("나만의 단어장에 단어 삭제 DOCS 200-DO-NOT-EXIST")
    void myWord_delete_fail() throws Exception {
        // given
        MyWordDeleteResponse response = createMyWordDeleteFailResponse();
        when(myWordService.delete(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                delete(MYWORD_URL + "/{myWordId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        // then
        result.andExpect(status().isOk())
                .andDo(document("MyWord-delete-200-do-not-exist",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("myWordId").description("삭제 요청할 단어 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("삭제를 요청한 단어의 정보").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.wordId").description("삭제를 요청한 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.name").description("NULL").type(JsonFieldType.NULL),
                                fieldWithPath("data.morpheme").description("NULL").type(JsonFieldType.NULL),
                                fieldWithPath("data.mean").description("NULL").type(JsonFieldType.NULL),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.search-myWord.href").description("Search-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.search-myWord.type").description("Search-MyWord Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("나만의 단어장에 단어 검색 DOCS")
    void myWord_search() throws Exception {
        // given
        MyWordSearchRequest request = new MyWordSearchRequest("2", "2");
        MyWordSearchResponse response = createMyWordSearchResponse();
        when(myWordService.searchByPagenation(any(), any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(MYWORD_URL + "?page=0&count=10")
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("MyWord-search-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        requestParameters(
                                parameterWithName("page").description("0부터 시작하는 페이지 index"),
                                parameterWithName("count").description("페이지당 데이터의 수")
                        ),
                        requestFields(
                                fieldWithPath("name").description("검색을 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("morpheme").description("검색 요청한 단어의 품사").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("datum").description("조회 결과 데이터").type(JsonFieldType.OBJECT),
                                fieldWithPath("datum.content").description("조회된 단어 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("datum.content[].wordId").description("검색된 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("datum.content[].name").description("검색된 단어의 이름").type(JsonFieldType.STRING),
                                fieldWithPath("datum.content[].morpheme").description("검색된 단어의 형태소").type(JsonFieldType.STRING),
                                fieldWithPath("datum.content[].mean").description("검색된 단어의 의미").type(JsonFieldType.STRING),
                                fieldWithPath("datum.pageable").description("페이징 정보").ignored(),
                                fieldWithPath("datum.last").description("마지막 페이지 여부").ignored(),
                                fieldWithPath("datum.totalPages").description("전체 페이지 수").ignored(),
                                fieldWithPath("datum.totalElements").description("전체 항목 수").ignored(),
                                fieldWithPath("datum.size").description("페이지 크기").ignored(),
                                fieldWithPath("datum.number").description("현재 페이지 번호").ignored(),
                                fieldWithPath("datum.sort").description("페이징된 결과의 정렬 정보").ignored(),
                                fieldWithPath("datum.sort.empty").description("정렬 정보가 비어있는지 여부").ignored(),
                                fieldWithPath("datum.sort.sorted").description("정렬된 정보가 있는지 여부").ignored(),
                                fieldWithPath("datum.sort.unsorted").description("정렬되지 않은 정보가 있는지 여부").ignored(),
                                fieldWithPath("datum.first").description("첫 번째 페이지 여부").ignored(),
                                fieldWithPath("datum.numberOfElements").description("현재 페이지에 포함된 항목 수").ignored(),
                                fieldWithPath("datum.empty").description("결과가 비어있는지 여부").ignored(),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    private MyWordAddRequest createMyWordAddRequest() {
        return new MyWordAddRequest("단어", "품사", "의미");
    }

    private MyWordAddResponse createMyWordAddResponse(String msg) {
        return new MyWordAddResponse(msg, new MyWordResponse(1L, "단어", "품사", "의미"));
    }

    private MyWordDeleteResponse createMyWordDeleteResponse() {
        MyWordResponse data = new MyWordResponse(1L, "단어", "품사", "의미");
        return new MyWordDeleteResponse(data.getWordId() + MYWORD_DELETE_SUCCESS_MSG, data);
    }

    private MyWordDeleteResponse createMyWordDeleteFailResponse() {
        MyWordResponse data = new MyWordResponse(1L, null, null, null);
        return new MyWordDeleteResponse(data.getWordId() + MYWORD_DEELETE_DO_NOT_EXIST_MSG, data);
    }

    private MyWordSearchResponse createMyWordSearchResponse() {
        List<MyWordResponse> dataList = new ArrayList<>();
        MyWordResponse dto = new MyWordResponse(1L, "단어", "품사", "의미");
        dataList.add(dto);

        return new MyWordSearchResponse(MYWROD_SEARCH_SUCCESS, new PageImpl<>(dataList));
    }
}
