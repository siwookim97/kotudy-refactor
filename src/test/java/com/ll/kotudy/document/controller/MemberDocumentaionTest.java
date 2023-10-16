package com.ll.kotudy.document.controller;

import com.ll.kotudy.document.utils.DocsControllerTestBase;
import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import com.ll.kotudy.member.dto.response.JoinResponse;
import com.ll.kotudy.member.dto.response.LoginResponse;
import com.ll.kotudy.member.dto.response.SearchMemberResponse;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.util.exception.ErrorCode;
import com.ll.kotudy.word.domain.MemberMyWord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.ll.kotudy.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberDocumentaionTest extends DocsControllerTestBase {

    private static final String MEMBERAUTH_JOIN_POST = "/api/v1/member";
    private static final String MEMBERAUTH_SEARCH_ID_POST = "/api/v1/member/{id}";
    private static final String MEMBERAUTH_LOGIN_POST = "/api/v1/member/login";
    private static final String MEMBERAUTH_JOIN_POST_MSG = "회원 가입이 완료되었습니다.";
    private static final String MEMBERAUTH_LOGIN_POST_MSG = "로그인이 완료되었습니다.";
    private static final String MEMBERAUTH_JOIN_POST_FAIL_MSG = "회원님은 이미 있습니다.";
    private static final String MEMBERAUTH_LOGIN_POST_FAIL_INVALID_PASSWORD_MSG = "패스워드를 잘못 입력했습니다.";
    private static final String MEMBERAUTH_LOGIN_POST_FAIL_USER_NOT_FOUND_MSG = "회원이(가) 없습니다.";
    private static final String MEMBERAUTH_SEARCH_MEMBER_GET_MSG = "검색한 회원의 정보는 다음과 같습니다.";

    @Test
    @DisplayName("회원가입 DOCS")
    public void memberAuth_post_join() throws Exception {
        // given
        MemberJoinRequest request = createMemberJoinRequest();
        JoinResponse response = createMemberJoinResponse();
        when(memberService.join(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                post(MEMBERAUTH_JOIN_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(document("MemberAuth-join-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("회원가입을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("회원가입을 위한 회원 Password").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("id").description("회원가입이 완료된 회원 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("username").description("회원가입이 완료된 회원 username").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("회원가입이 완료된 회원 Encoding Password").type(JsonFieldType.STRING),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING),
                                fieldWithPath("_links.login").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.login.href").description("Login Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.login.type").description("Login Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 조회(ID) DOCS")
    public void memberAuth_get_search_id() throws Exception {
        // given
        MemberJoinRequest request = createMemberJoinRequest();
        SearchMemberResponse response = createSearchMemberResponse();
        when(memberService.search(any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                get(MEMBERAUTH_SEARCH_ID_POST, response.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("MemberAuth-search-id-200",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("회원가입을 위한 회원 ID").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("회원가입을 위한 회원 Password").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("msg").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("id").description("검색한 회원의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("username").description("검색한 회원의 username").type(JsonFieldType.STRING),
                                fieldWithPath("memberMyWordList").description("회원이 저장한 단어의 정보").type(JsonFieldType.ARRAY),
                                fieldWithPath("_links").description("Hyper Links").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self").description("Hyper Link 이름").type(JsonFieldType.OBJECT),
                                fieldWithPath("_links.self.href").description("Self Hyper Link").type(JsonFieldType.STRING),
                                fieldWithPath("_links.self.type").description("Self Hyper Link Type").type(JsonFieldType.STRING)
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 DOCS-4xx")
    public void memberAuth_post_join_4xx() throws Exception {
        // given
        MemberJoinRequest request = createMemberJoinRequest();
        when(memberService.join(any(), any())).thenThrow(
                new AppException(ErrorCode.USERNAME_DUPLICATED,
                        request.getUsername() + MEMBERAUTH_JOIN_POST_FAIL_MSG)
        );

        // when
        ResultActions result = this.mockMvc.perform(
                post(MEMBERAUTH_JOIN_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().is4xxClientError())
                .andDo(document("MemberAuth-join-4xx",
                        getDocumentRequest(),
                        getDocumentResponse(),
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
    @DisplayName("로그인 DOCS")
    public void memberAuth_post_login() throws Exception {
        // given
        MemberLoginRequest request = createMemberLoginRequest();
        LoginResponse response = createMemberLoginResponse();
        when(memberService.login(any(), any())).thenReturn(response);

        // when
        ResultActions result = this.mockMvc.perform(
                post(MEMBERAUTH_LOGIN_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("MemberAuth-login-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
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
    @DisplayName("로그인 DOCS-4xx-INVALID-PASSWORD")
    public void memberAuth_login_4xx_invalid_password() throws Exception {
        // given
        MemberLoginRequest request = createMemberLoginRequest();
        when(memberService.login(any(), any())).thenThrow(
                new AppException(ErrorCode.INVALID_PASSWORD, MEMBERAUTH_LOGIN_POST_FAIL_INVALID_PASSWORD_MSG)
        );

        // when
        ResultActions result = this.mockMvc.perform(
                post(MEMBERAUTH_LOGIN_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().is4xxClientError())
                .andDo(document("MemberAuth-login-4xx-invalid-password",
                        getDocumentRequest(),
                        getDocumentResponse(),
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
    @DisplayName("로그인 DOCS-4xx-USER-NOT-FOUND")
    public void memberAuth_login_4xx_usr_not_found() throws Exception {
        // given
        MemberLoginRequest request = createMemberLoginRequest();
        when(memberService.login(any(), any()))
                .thenThrow(
                        new AppException(
                                ErrorCode.BODY_BAD_REQUEST,
                                request.getUsername() + MEMBERAUTH_LOGIN_POST_FAIL_USER_NOT_FOUND_MSG)
                );

        // when
        ResultActions result = this.mockMvc.perform(
                post(MEMBERAUTH_LOGIN_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().is4xxClientError())
                .andDo(document("MemberAuth-login-4xx-user-not-found",
                        getDocumentRequest(),
                        getDocumentResponse(),
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


    private MemberJoinRequest createMemberJoinRequest() {
        return new MemberJoinRequest("Hong-Gildong", "password");
    }

    private JoinResponse createMemberJoinResponse() {
        return new JoinResponse(MEMBERAUTH_JOIN_POST_MSG, 1L, "Hong-Gildong", "password");
    }

    private MemberLoginRequest createMemberLoginRequest() {
        return new MemberLoginRequest("Hong-Gildong", "password");
    }

    private LoginResponse createMemberLoginResponse() {
        return new LoginResponse(
                MEMBERAUTH_LOGIN_POST_MSG,
                "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmNhQ5uHZELuMKK_1O_Wcd9lx0aggkg");
    }

    private SearchMemberResponse createSearchMemberResponse() {
        List<MemberMyWord> memberMyWordList = new ArrayList<>();
//        memberMyWordList.add(new MemberMyWord(1L, 1L, 1L));

        return new SearchMemberResponse(
                MEMBERAUTH_SEARCH_MEMBER_GET_MSG,
                1L, "Hong-Gildong", new ArrayList<>()
        );
    }
}
