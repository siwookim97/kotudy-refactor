package com.ll.kotudy.word.domain;

import com.ll.kotudy.config.QuerydslConfig;
import com.ll.kotudy.word.dto.TodayWordDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class TodayWordRepositoryTest {

    @Autowired
    private TodayWordRepository todayWordRepository;

    @Test
    @DisplayName("오늘의 단어 저장 확인 테스트")
    void saveTodayWordTest() {
        // given
        TodayWordDto todayWordDto = new TodayWordDto("단어", "품사", "의미");
        TodayWord createdTodayWord = new TodayWord(todayWordDto);

        // when
        TodayWord savedTodayWord = todayWordRepository.save(createdTodayWord);

        // then
        assertAll(
                () -> assertThat(savedTodayWord.getName()).isEqualTo("단어"),
                () -> assertThat(savedTodayWord.getMorpheme()).isEqualTo("품사"),
                () -> assertThat(savedTodayWord.getMean()).isEqualTo("의미")
        );
    }

    @Test
    @DisplayName("오늘의 단어 검색 확인 테스트")
    void findTodayWordTest() {
        // given
        TodayWordDto todayWordDto = new TodayWordDto("단어", "품사", "의미");
        TodayWord createdTodayWord = new TodayWord(todayWordDto);
        todayWordRepository.save(createdTodayWord);

        // when
        Optional<TodayWord> foundTodayWord = todayWordRepository.findById(createdTodayWord.getId());

        // then
        assertAll(
                () -> assertThat(foundTodayWord.isPresent()).isTrue(),
                () -> assertThat(foundTodayWord.get().getName()).isEqualTo("단어"),
                () -> assertThat(foundTodayWord.get().getMorpheme()).isEqualTo("품사"),
                () -> assertThat(foundTodayWord.get().getMean()).isEqualTo("의미")
        );
    }
}