package com.ll.kotudy.word.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public class DictionaryOpenApiControllerTest {

    private MockMvc mockMvc;

    final private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUpMockMvcForRestDocs(WebApplicationContext webApplicationContext,
                                 RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    public void dictionary_word() throws Exception {
        String response = "표준 한국어 대사전 Open API를 통해 단어 사랑의 검색결과는 다음과 같습니다.";

        mockMvc.perform(get("/api/v1/dictionary/word")
                        .param("q", "사랑")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("Dictionary-word",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("q").description("찾고자 하는 단어")
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("한국어 대사전으로 찾은 동음 이의어들의 집합").type(JsonFieldType.ARRAY),
                                fieldWithPath("data[].targetCode").description("찾고자 하는 단어의 한국어 대사전 TargetCode").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].word").description("찾고자 하는 단어").type(JsonFieldType.STRING),
                                fieldWithPath("data[].pronunciation").description("찾고자 하는 단어의 발음").type(JsonFieldType.STRING),
                                fieldWithPath("data[].pos").description("찾고자 하는 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("data[].wordSenceList[]").description("해당 단어의 뜻").type(JsonFieldType.ARRAY),
                                fieldWithPath("data[].wordSenceList[].senseOrder").description("해당 단어의 뜻 순서").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].wordSenceList[].definition").description("해당 단어의 뜻").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }
}
