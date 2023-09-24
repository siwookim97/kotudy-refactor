package com.ll.kotudy.word.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MyWordTest {

    @Test
    @DisplayName("단어 생성 테스트 (성공)")
    void createMyWordTest() {
        // given & when & then
        assertDoesNotThrow(() -> MyWord.builder().name("단어").mean("의미").morpheme("품사").build());
    }

    @Test
    @DisplayName("단어 Score Plus 테스트")
    void plusCountMyWordTest() {
        // given
        MyWord createdMyWord = MyWord.builder()
                .name("단어")
                .mean("의미")
                .morpheme("품사")
                .build();

        // when
        createdMyWord.plusCount();

        // then
        assertThat(createdMyWord.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("단어 Score Minus 테스트")
    void minusCountMyWordTest() {
        // given
        MyWord createdMyWord = MyWord.builder()
                .name("단어")
                .mean("의미")
                .morpheme("품사")
                .build();

        // when
        createdMyWord.plusCount();
        createdMyWord.minusCount();

        // then
        assertThat(createdMyWord.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("단어 Score 0일 경우 내려가지 Minus되지 않음")
    void minusCountZeroMyWordTest() {
        // given
        MyWord createdMyWord = MyWord.builder()
                .name("단어")
                .mean("의미")
                .morpheme("품사")
                .build();

        // when
        createdMyWord.minusCount();

        // then
        assertThat(createdMyWord.getCount()).isEqualTo(0);
    }
}