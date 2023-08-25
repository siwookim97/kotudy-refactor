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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                                fieldWithPath("username").description("Username for member").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("Password for member").type(JsonFieldType.STRING)
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
        String response = "{\"msg\":\"회원 가입이 완료되었습니다.\",\"id\":1,\"username\":\"홍길동\"}";

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                )
                .andExpect(status().isOk())
              //  .andExpect(content().json(response))
                .andDo(print());
    }

//    @Test
//    @DisplayName("회원가입 성공")
//    @WithMockUser
//    void join() throws Exception {
//        String username = "honggildong";
//        String password = "1234";
//
//        mockMvc.perform(post("/api/v1/users/join")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(username, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("회원가입 실패 - USERNAME 중복")
//    @WithMockUser
//    void joinFail_username() throws Exception {
//        String username = "honggildong";
//        String password = "1234";
//
//        when(memberService.join(any(), any()))
//                .thenThrow(new RuntimeException("해당 userId가 중복됩니다."));
//
//        mockMvc.perform(post("/api/v1/users/join")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(username, password))))
//                .andDo(print())
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @DisplayName("로그인 성공")
//    @WithMockUser
//    void login_success() throws Exception {
//        String username = "honggildong";
//        String password = "1234";
//
//        when(memberService.login(any(), any()))
//                .thenReturn(new LoginResponse("honggildong", "accessToken"));
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(username, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("로그인 실패 - USERNAME 없음")
//    @WithMockUser
//    void login_fail_username() throws Exception {
//        String username = "honggildong";
//        String password = "1234";
//
//        when(memberService.login(any(), any()))
//                .thenThrow(new AppException(ErrorCode.BODY_BAD_REQUEST, ""));
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(username, password))))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("로그인 실패 - PASSWORD 불일치")
//    @WithMockUser
//    void login_fail_password() throws Exception {
//        String username = "honggildong";
//        String password = "1234";
//
//        when(memberService.login(any(), any()))
//                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ""));
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(username, password))))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
}