package com.ll.kotudy.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class MemberAuthControllerTest {

    @Autowired
    private MemberRepository memberRepository;

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

    @BeforeEach
    public void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    public void memberAuth_join_2xx() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        String response = "회원 가입이 완료되었습니다.";

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("Member-join-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("회원가입을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("회원가입을 위한 회원 Password").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("id").description("회원가입이 완료된 회원 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("username").description("회원가입이 완료된 회원 Password").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    public void memberAuth_join_4xx() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        String response = "홍길동회원님은 이미 있습니다.";

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andDo(print());

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.msg").value(response))
                .andDo(document("Member-join-4xx",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("회원가입을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("회원가입을 위한 회원 Password").type(JsonFieldType.STRING)
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
    public void memberAuth_login_2xx() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());

        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer1234");

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(("로그인이 완료되었습니다.")))
                .andDo(document("Member-login-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("로그인을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("로그인을 위한 회원 Password").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("accessToken").description("회원의 AccessToken").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    public void memberAuth_login_4xx() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());

        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("홍길동", "qwer");

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.msg").value(("패스워드를 잘못 입력했습니다.")))
                .andDo(document("Member-login-4xx-ver2",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("로그인을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("로그인을 위한 회원 Password").type(JsonFieldType.STRING)
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
    public void memberAuth_login_4xx_ver2() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());

        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("Hong Gildong", "qwer");

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.msg").value(("Hong Gildong회원이(가) 없습니다.")))
                .andDo(document("Member-login-4xx",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("로그인을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("로그인을 위한 회원 Password").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("timeStamp").description("API 요청 시간").type(JsonFieldType.STRING),
                                fieldWithPath("httpStatus").description("HTTP 상태 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("errorCode").description("HTTP 에러 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }
}
