package com.ll.kotudy.word.domain;

import com.ll.kotudy.config.QuerydslConfig;
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
class MyWordRepositoryTest {

    @Autowired
    private MyWordRepository myWordRepository;

    @Test
    @DisplayName("나만의 단어장에 단어 저장 테스트")
    void saveMyWord() {
        // given
        MyWord createdMyWord = new MyWord("단어", "품사", "의미");

        // when
        MyWord savedMember = myWordRepository.save(createdMyWord);

        // then
        assertAll(
                () -> assertThat(savedMember.getCount()).isEqualTo(0),
                () -> assertThat(savedMember.getName()).isEqualTo("단어"),
                () -> assertThat(savedMember.getMorpheme()).isEqualTo("품사"),
                () -> assertThat(savedMember.getMean()).isEqualTo("의미")
        );
    }

    @Test
    @DisplayName("나만의 단어장에 단어 찾기 테스트 (성공)")
    void findByNameAndMorphemeAndMeanMyWord() {
        // given
        MyWord createdMyWord = new MyWord("단어", "품사", "의미");
        myWordRepository.save(createdMyWord);

        // when
        Optional<MyWord> foundOptionalMyWord = myWordRepository.findByNameAndMorphemeAndMean("단어", "품사", "의미");

        // then
        assertAll(
                () -> assertThat(foundOptionalMyWord.isPresent()).isTrue(),
                () -> assertThat(foundOptionalMyWord.get().getCount()).isEqualTo(0),
                () -> assertThat(foundOptionalMyWord.get().getName()).isEqualTo("단어"),
                () -> assertThat(foundOptionalMyWord.get().getMorpheme()).isEqualTo("품사"),
                () -> assertThat(foundOptionalMyWord.get().getMean()).isEqualTo("의미")
        );
    }

    @Test
    @DisplayName("나만의 단어장에 단어 찾기 테스트 (실패-해당 자료 없음)")
    void findByNameAndMorphemeAndMeanMyWord_fail() {
        // given
        MyWord createdMyWord = new MyWord("단어", "품사", "의미");
        myWordRepository.save(createdMyWord);

        // when
        Optional<MyWord> foundOptionalMyWord = myWordRepository.findByNameAndMorphemeAndMean("품사", "단어", "의미");

        // then
        assertThat(foundOptionalMyWord.isPresent()).isFalse();
    }
}
