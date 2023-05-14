package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.QuizWordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MyWordRepositoryTest {

    @Autowired
    MyWordRepository myWordRepository;

    @BeforeEach
    void init() {
        MyWord myWordA = new MyWord("달리다", "동사", "뜀박질을 하다");
        myWordRepository.save(myWordA);

        for (int i = 0; i < 10000; i++) {
            myWordRepository.save(new MyWord("동물" + i, "명사",
                    "사람을 제외한 길짐승, 날짐승, 물짐승 따위를 통틀어 이르는 말." + i));
        }
        myWordRepository.flush();
    }

    @Test
    @DisplayName("name과 morpheme필드를 사용해 db에 있는지 확인 쿼리 테스트")
    void existsByNameAndMorphemeTest() {

        boolean exist = myWordRepository.existsByNameAndMorpheme("동물1", "명사");
        boolean nonExist = myWordRepository.existsByNameAndMorpheme("달리다", "명사");

        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    @DisplayName("퀴즈를 위한 단어 가져오는 쿼리 테스트")
    void findMyWordDistnctRandomForQuizTest() {

        List<QuizWordDto> myWordDistnctRandomForQuiz = myWordRepository.findMyWordDistnctRandomForQuiz();

        assertThat(myWordDistnctRandomForQuiz.size()).isEqualTo(40);
        for (QuizWordDto quizWordDto : myWordDistnctRandomForQuiz) {
            assertThat(quizWordDto).isInstanceOf(QuizWordDto.class);
        }
    }

    @Test
    @DisplayName("name과 morpheme필드를 사용해 삭제하는 쿼리 테스트")
    void deleteByNameAndMorphemeTest() {

        myWordRepository.deleteByNameAndMorpheme("동물20", "명사");
        myWordRepository.deleteByNameAndMorpheme("동물21", "명사");
        List<MyWord> findMyWord = myWordRepository.findAll();

        assertThat(findMyWord.size()).isEqualTo(9999);
        assertThat(myWordRepository.existsByNameAndMorpheme("동물20", "명사")).isFalse();
        assertThat(myWordRepository.existsByNameAndMorpheme("동물21", "명사")).isFalse();
        assertThat(myWordRepository.existsByNameAndMorpheme("동물22", "명사")).isTrue();
    }
}