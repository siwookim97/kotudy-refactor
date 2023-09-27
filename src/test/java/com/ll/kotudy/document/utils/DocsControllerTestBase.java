package com.ll.kotudy.document.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.word.service.DictionaryService;
import com.ll.kotudy.word.service.MyWordService;
import com.ll.kotudy.word.service.QuizService;
import com.ll.kotudy.word.service.TodayWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

@WebMvcTest
@AutoConfigureRestDocs
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

    final protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUpMockMvcForRestDocs(WebApplicationContext webApplicationContext,
                                 RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

}
