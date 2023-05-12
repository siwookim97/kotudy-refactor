package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.service.dto.QuizWordDto;

import java.util.List;

public interface MyWordRepositoryCustom {

    List<QuizWordDto> findMyWordDistnctRandomForQuiz();
}