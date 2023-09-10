package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.QuizWordDto;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordResponse;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyWordRepositoryCustom {

    List<QuizWordDto> findMyWordDistinctRandomForQuiz();

    List<TodayWordDto> findRandomDistinctMyWords();

    Page<MyWordResponse> findMyWordByConditionMyWord(MyWordSearchRequest condition, Pageable pageable, Long memberId);
}