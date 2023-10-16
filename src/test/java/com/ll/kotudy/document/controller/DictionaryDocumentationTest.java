package com.ll.kotudy.document.controller;

import com.ll.kotudy.document.utils.DocsControllerTestBase;
import com.ll.kotudy.word.dto.SearchedWordDto;
import com.ll.kotudy.word.dto.WordSenceDto;
import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ll.kotudy.document.utils.ApiDocumentUtils.*;
import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DictionaryDocumentationTest extends DocsControllerTestBase {

    private static final String DICTIONARY_GET_URL = "/api/v1/dictionary/word";
    private static final String DICTIONARY_GET_PREFIX_MSG = "표준 한국어 대사전 Open API를 통해 단어 ";
    private static final String DICTIONARY_GET_SUFFIX_MSG = "의 검색결과는 다음과 같습니다.";

    @Test
    @DisplayName("한국어 대사전 단어 검색 DOCS")
    void dictionary_get() throws Exception {
        // given
        SearchedWordsResponse response = createSearchedWordsResponse();
        when(dictionaryService.searchWords(any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(DICTIONARY_GET_URL)
                        .param("q", "사랑")
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("Dictionary-word-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("q").description("찾고자 하는 단어")
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("datum").description("한국어 대사전으로 찾은 동음 이의어들의 집합").type(JsonFieldType.ARRAY),
                                fieldWithPath("datum[].targetCode").description("찾고자 하는 단어의 한국어 대사전 TargetCode").type(JsonFieldType.NUMBER),
                                fieldWithPath("datum[].word").description("찾고자 하는 단어").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].pronunciation").description("찾고자 하는 단어의 발음").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].pos").description("찾고자 하는 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("datum[].wordSenceList[]").description("해당 단어의 뜻").type(JsonFieldType.ARRAY),
                                fieldWithPath("datum[].wordSenceList[].senseOrder").description("해당 단어의 뜻 순서").type(JsonFieldType.NUMBER),
                                fieldWithPath("datum[].wordSenceList[].definition").description("해당 단어의 뜻").type(JsonFieldType.STRING),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.add-myWord").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.add-myWord.href").description("Add-MyWord Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.add-myWord.type").description("Add-MyWord Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("한국어 대사전 단어 검색 DOCS-5xx")
    public void dictionary_get_5xx() throws Exception {
        // given
        when(dictionaryService.searchWords(any())).thenThrow(RuntimeException.class);

        // when
        ResultActions result = this.mockMvc.perform(
                get(DICTIONARY_GET_URL)
                        .param("q", "")
        );

        //then
        result.andExpect(status().is5xxServerError())
                .andDo(document("Dictionary-word-5xx",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("q").description("찾고자 하는 단어")
                        ),
                        responseFields(
                                fieldWithPath("timeStamp").description("API 요청 시간").type(JsonFieldType.STRING),
                                fieldWithPath("httpStatus").description("HTTP 상태 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("errorCode").description("HTTP 에러 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    private SearchedWordsResponse createSearchedWordsResponse() {
        WordSenceDto wordSenceDto1 = new WordSenceDto(1, "땅 위로 펼쳐진 무한히 넓은 공간.");
        WordSenceDto wordSenceDto2 = new WordSenceDto(2, "절대적인 존재, 하느님.");
        List<WordSenceDto> wordSenceDtos = new ArrayList<>(Arrays.asList(wordSenceDto1, wordSenceDto2));
        SearchedWordDto searchedWordDto = new SearchedWordDto(
                20311,
                "하늘",
                "하늘",
                "명사",
                wordSenceDtos
        );
        List<SearchedWordDto> datum = new ArrayList<>();
        datum.add(searchedWordDto);

        return new SearchedWordsResponse(DICTIONARY_GET_PREFIX_MSG + "하늘" + DICTIONARY_GET_SUFFIX_MSG, datum);
    }
}
