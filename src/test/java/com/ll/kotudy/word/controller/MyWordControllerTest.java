package com.ll.kotudy.word.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
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

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class MyWordControllerTest {

    private MockMvc mockMvc;
    private static String token;

    @Autowired
    private MemberRepository memberRepository;

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
    }

    @Test
    @WithMockUser(username = "홍길동", roles = {"USER"})
    public void mywWord_add_2xx() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");
        MyWordAddRequest myWordAddRequest = new MyWordAddRequest("이름", "품사", "의미");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        token = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.accessToken");

        mockMvc.perform(post("/api/v1/myWord")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWordAddRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(("이름 단어 추가가 성공하였습니다.")))
                .andDo(document("MyWord-add-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
                                fieldWithPath("data.mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "홍길동", roles = {"USER"})
    public void mywWord_add_fail() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");
        MyWordAddRequest myWordAddRequest = new MyWordAddRequest("name", "morpheme", "mean");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        token = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.accessToken");

        mockMvc.perform(post("/api/v1/myWord")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWordAddRequest))
                )
                .andDo(print());

        mockMvc.perform(post("/api/v1/myWord")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWordAddRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(("name 단어 추가가 실패하였습니다.(이미 단어장에 존재)")))
                .andDo(document("MyWord-add-fail",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
                                fieldWithPath("data.mean").description("저장을 요청한 단어의 의미").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "홍길동", roles = {"USER"})
    public void mywWord_add_4xx() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");
        MyWordAddRequest myWordAddRequest = new MyWordAddRequest("name", "morpheme", "mean");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        token = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.accessToken");

        mockMvc.perform(post("/api/v1/myWord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWordAddRequest))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.msg").value(("올바른 요청 Header 형식이 아닙니다.")))
                .andDo(document("MyWord-add-4xx",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
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
    @WithMockUser(username = "홍길동", roles = {"USER"})
    public void mywWord_delete() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");
        MyWordAddRequest myWordAddRequest = new MyWordAddRequest("이름", "품사", "의미");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        token = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.accessToken");
        mockMvc.perform(post("/api/v1/myWord")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(myWordAddRequest))
        )
                        .andDo(print());

        mockMvc.perform(delete("/api/v1/myWord/{myWordId}", 1)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(("1번호의 단어 삭제를 성공하였습니다.")))
                .andDo(document("MyWord-delete-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("myWordId").description("삭제 요청할 단어 ID")
                        ),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("삭제를 요청한 단어의 정보").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.wordId").description("삭제를 요청한 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.name").description("삭제를 요청한 단어").type(JsonFieldType.STRING),
                                fieldWithPath("data.morpheme").description("삭제를 요청한 단어의 품사").type(JsonFieldType.STRING),
                                fieldWithPath("data.mean").description("삭제를 요청한 단어의 의미").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "홍길동", roles = {"USER"})
    public void mywWord_delete_fail() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");
        MyWordAddRequest myWordAddRequest = new MyWordAddRequest("이름", "품사", "의미");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andReturn();

        token = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.accessToken");
        mockMvc.perform(post("/api/v1/myWord")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWordAddRequest))
                )
                .andDo(print());

        mockMvc.perform(delete("/api/v1/myWord/{myWordId}", 10)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(("10번의 단어 삭제가 실패하였습니다. (나만의 단어장에 존재하지 않는 단어입니다)")))
                .andDo(document("MyWord-delete-fail",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("myWordId").description("삭제 요청할 단어 ID")
                        ),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access 토큰")),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("삭제를 요청한 단어의 정보").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.wordId").description("삭제를 요청한 단어의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.name").description("NULL").type(JsonFieldType.NULL),
                                fieldWithPath("data.morpheme").description("NULL").type(JsonFieldType.NULL),
                                fieldWithPath("data.mean").description("NULL").type(JsonFieldType.NULL)
                        )))
                .andDo(print());
    }
}