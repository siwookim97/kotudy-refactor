package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.service.dto.QuizWordDto;
import org.junit.jupiter.api.BeforeEach;
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
    }

    @Test
    void existsByNameAndMorphemeTest() {

        boolean exist = myWordRepository.existsByNameAndMorpheme("동물1", "명사");
        boolean nonExist = myWordRepository.existsByNameAndMorpheme("달리다", "명사");

        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void findMyWordRandomForQuiz_Querydsl() {

        List<QuizWordDto> myWordDistnctRandomForQuiz = myWordRepository.findMyWordDistnctRandomForQuiz();

        assertThat(myWordDistnctRandomForQuiz.size()).isEqualTo(40);
        for (QuizWordDto quizWordDto : myWordDistnctRandomForQuiz) {
            assertThat(quizWordDto).isInstanceOf(QuizWordDto.class);
        }
    }
}