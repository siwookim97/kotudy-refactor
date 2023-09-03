package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.QuizWordDto;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ll.kotudy.word.domain.QMyWord.*;

@RequiredArgsConstructor
public class MyWordRepositoryImpl implements MyWordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuizWordDto> findMyWordDistinctRandomForQuiz() {

        return queryFactory
                .select(Projections.constructor(QuizWordDto.class, myWord.name, myWord.morpheme, myWord.mean))
                .distinct()
                .from(myWord)
                .orderBy(Expressions.numberTemplate(Integer.class, "RAND()").asc())
                .limit(40)
                .fetch();
    }

    @Override
    public Page<MyWordSearchResponse> findMyWordByCondition(MyWordSearchRequest condition, Pageable pageable) {

        return null;
    }

    @Override
    public List<TodayWordDto> findRandomDistinctMyWords() {

        return queryFactory
                .select(Projections.constructor(TodayWordDto.class, myWord.name, myWord.morpheme, myWord.mean))
                .distinct()
                .from(myWord)
                .orderBy(Expressions.numberTemplate(Integer.class, "RAND()").asc())
                .limit(10)
                .fetch();
    }
}
