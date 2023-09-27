package com.ll.kotudy.word.service;

import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class DictionaryServiceImplTest {

    @Autowired
    DictionaryServiceImpl dictionaryService;

    @Test
    @DisplayName("표준한국어대사전 OpenAPI 테스트 - 검색 결과 없음, 실패1")
    void searchWordOpenApiFailTest() throws Exception {
        // given
        String q = "뚫훓";

        // when
        SearchedWordsResponse response = dictionaryService.searchWords(q);

        // then
        assertThat(response.getMsg()).endsWith("검색 결과가 없습니다.");
        assertThat(response.getDatum().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("표준한국어대사전 OpenAPI 테스트 - 검색 결과 없음 실패2")
    void searchWordOpenApiBlankFailTest() throws Exception {
        // given
        String q = "no reult";

        // when
        SearchedWordsResponse response = dictionaryService.searchWords(q);
        // then
        assertThat(response.getMsg()).endsWith("검색 결과가 없습니다.");
        assertThat(response.getDatum().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("표준한국어대사전 OpenAPI 테스트 - 성공")
    void searchWordOpenApiSuccessTest() throws Exception {
        // given
        String q = "사랑";

        // when
        SearchedWordsResponse response = dictionaryService.searchWords(q);

        // then
        assertThat(response.getMsg()).endsWith("의 검색결과는 다음과 같습니다.");
        assertThat(response.getDatum().size()).isNotEqualTo(0);
    }
}