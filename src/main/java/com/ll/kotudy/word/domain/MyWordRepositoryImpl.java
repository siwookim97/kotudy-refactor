package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.QuizWordDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.kotudy.word.domain.QMyWord.*;

@RequiredArgsConstructor
public class MyWordRepositoryImpl implements MyWordRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuizWordDto> findMyWordDistnctRandomForQuiz() {
        return queryFactory
                .select(Projections.constructor(QuizWordDto.class, myWord.name, myWord.morpheme, myWord.mean))
                .distinct()
                .from(myWord)
                .orderBy(Expressions.numberTemplate(Integer.class, "RAND()").asc())
                .limit(40)
                .fetch();
    }
}
