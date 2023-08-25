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
    public void memberAuth_join() throws Exception {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("홍길동", "qwer1234");
        String response = "{\"msg\":\"회원 가입이 완료되었습니다.\",\"id\":1,\"username\":\"홍길동\"}";

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(response))
                .andDo(document("Member-join",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("Username for member join").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("Password for member join").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("Message of request").type(JsonFieldType.STRING),
                                fieldWithPath("id").description("Id of member").type(JsonFieldType.NUMBER),
                                fieldWithPath("username").description("Username of member").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    public void memberAuth_login() throws Exception {
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
                .andDo(document("Member-login",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("Username for member login").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("Password for member login").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("Message of request").type(JsonFieldType.STRING),
                                fieldWithPath("accessToken").description("AccessToken of member").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }
}
