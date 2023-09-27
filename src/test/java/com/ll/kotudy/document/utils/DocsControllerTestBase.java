package com.ll.kotudy.document.utils;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.word.service.DictionaryService;
import com.ll.kotudy.word.service.MyWordService;
import com.ll.kotudy.word.service.QuizService;
import com.ll.kotudy.word.service.TodayWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
public class DocsControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected TodayWordService todayWordService;

    @MockBean
    protected QuizService quizService;

    @MockBean
    protected DictionaryService dictionaryService;

    @MockBean
    protected MyWordService myWordService;

    @BeforeEach
    void setUpMockMvcForRestDocs(WebApplicationContext webApplicationContext,
                                 RestDocumentationContextProvider restDocumentationContextProvider) throws ServletException {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

}
