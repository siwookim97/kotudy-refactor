package com.ll.kotudy.word.service;

import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.dto.QuizForm;
import com.ll.kotudy.word.dto.QuizWordDto;
import com.ll.kotudy.word.dto.response.QuizResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @InjectMocks
    private QuizService quizService;

    @Mock
    private MyWordRepository myWordRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("퀴즈 Form 생성 테스트 (성공)")
    void createFormTest() {
        // given
        List<QuizWordDto> quizWordDtoList = getQuizWordDtoList(40);

        when(myWordRepository.findMyWordDistinctRandomForQuiz()).thenReturn(quizWordDtoList);

        // when
        QuizResponse response = quizService.createForm();

        // then
        assertAll(
                () -> assertThat(response.getMsg()).endsWith("생성된 퀴즈입니다."),
                () -> assertThat(response.getDatum().size()).isEqualTo(10)
        );

        // verify
        verify(myWordRepository, times(1)).findMyWordDistinctRandomForQuiz();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 20, 39})
    @DisplayName("퀴즈 Form 생성 테스트 (실패 - 단어 부족)")
    void createForm40FailTest(int selectedWordNum) {
        // given
        List<QuizWordDto> quizWordDtoList = getQuizWordDtoList(selectedWordNum);

        when(myWordRepository.findMyWordDistinctRandomForQuiz()).thenReturn(quizWordDtoList);

        // when & then
        assertThrows(AppException.class, () -> quizService.createForm());

        // verify
        verify(myWordRepository, times(1)).findMyWordDistinctRandomForQuiz();
    }

    private List<QuizWordDto> getQuizWordDtoList(int selectedWordNum) {
        List<QuizWordDto> quizWordDtoList = new ArrayList<>();

        for (int i = 0; i < selectedWordNum; i++) {
            quizWordDtoList.add(new QuizWordDto("이름" + i, "품사" + i, "의미" + i));
        }

        return quizWordDtoList;
    }

    private QuizResponse getQuizResponse() {
        List<QuizForm> quizFormList = getQuizFormList();

        return new QuizResponse("나만의 단어장을 기반으로 생성된 퀴즈입니다.", null);
    }

    private List<QuizForm> getQuizFormList() {
        List<QuizForm> quizFormList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            QuizForm form = new QuizForm();
            List<String> choices = new ArrayList<>();

            form.setQuestion("의미" + i);
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    choices.add("단어" + i);
                }
            }
            form.setAnswerIndex(1);
            form.setChoices(choices);
        }

        return quizFormList;
    }
}