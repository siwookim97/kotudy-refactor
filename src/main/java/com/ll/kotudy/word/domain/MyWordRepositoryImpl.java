package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.QuizWordDto;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordResponse;
import com.ll.kotudy.word.dto.response.QMyWordResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ll.kotudy.word.domain.QMemberMyWord.memberMyWord;
import static com.ll.kotudy.word.domain.QMyWord.*;
import static io.jsonwebtoken.lang.Strings.hasText;

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
    public List<TodayWordDto> findRandomDistinctMyWords() {

        return queryFactory
                .select(Projections.constructor(TodayWordDto.class, myWord.name, myWord.morpheme, myWord.mean))
                .distinct()
                .from(myWord)
                .orderBy(Expressions.numberTemplate(Integer.class, "RAND()").asc())
                .limit(10)
                .fetch();
    }

    @Override
    public Page<MyWordResponse> findMyWordByConditionMyWord(MyWordSearchRequest condition, Pageable pageable, Long memberId) {
        List<MyWordResponse> content = queryFactory
                .select(new QMyWordResponse(
                        myWord.id.as("wordId"),
                        myWord.name,
                        myWord.morpheme,
                        myWord.mean
                ))
                .from(myWord)
                .leftJoin(myWord.memberMyWords, memberMyWord)
                .where(
                        memberIdEquals(memberId),
                        nameContains(condition.getName()),
                        morphemeContains(condition.getMorpheme())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(myWord.count())
                .from(myWord)
                .leftJoin(myWord.memberMyWords, memberMyWord)
                .where(
                        nameContains(condition.getName()),
                        morphemeContains(condition.getMorpheme()),
                        memberIdEquals(memberId)
                );

        return PageableExecutionUtils.getPage(content, pageable,
                countQuery::fetchOne);
    }

    private BooleanExpression nameContains(String name) {
        return hasText(name) ? memberMyWord.myWord.name.contains(name) : null;
    }

    private BooleanExpression morphemeContains(String morpheme) {
        return hasText(morpheme) ? memberMyWord.myWord.morpheme.contains(morpheme) : null;
    }

    private BooleanExpression memberIdEquals(Long memberId) {
        return memberMyWord.member.id.eq(memberId);
    }
}
