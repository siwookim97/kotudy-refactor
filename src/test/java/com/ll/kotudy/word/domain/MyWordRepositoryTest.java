package com.ll.kotudy.word.domain;

import com.ll.kotudy.config.QuerydslConfig;
import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.word.dto.QuizWordDto;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyWordRepositoryTest {

    @Autowired
    private MyWordRepository myWordRepository;

    @Autowired
    private MemberMyWordRepository memberMyWordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("단어장에 단어 저장 테스트")
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
    @DisplayName("단어장에 단어 검색 테스트 (성공)")
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
    @DisplayName("단어장에 단어 찾기 테스트 (실패-해당 자료 없음)")
    void findByNameAndMorphemeAndMeanMyWord_fail() {
        // given
        MyWord createdMyWord = new MyWord("단어", "품사", "의미");
        myWordRepository.save(createdMyWord);

        // when
        Optional<MyWord> foundOptionalMyWord = myWordRepository.findByNameAndMorphemeAndMean("품사", "단어", "의미");

        // then
        assertThat(foundOptionalMyWord.isPresent()).isFalse();
    }

    @Test
    @DisplayName("단어장에서 랜덤 40개 반환 테스트")
    void findMyWordDistinctRandomForQuizTest() {
        // given
        for (int i = 0; i < 50; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            myWordRepository.save(createdMyWord);
        }

        // when
        List<QuizWordDto> foundQuizWordDtoList = myWordRepository.findMyWordDistinctRandomForQuiz();

        // then
        assertThat(foundQuizWordDtoList.size()).isEqualTo(40);
    }

    @Test
    @DisplayName("단어장에서 랜덤 40개 반환 중복값 유무 테스트")
    void findMyWordDistinctRandomForQuizDistinctTest() {
        // given
        for (int i = 0; i < 50; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            myWordRepository.save(createdMyWord);
        }

        // when
        List<QuizWordDto> foundQuizWordDtoList = myWordRepository.findMyWordDistinctRandomForQuiz();

        // then
        assertThat(foundQuizWordDtoList.stream().distinct().count()).isEqualTo(40);
    }

    @Test
    @DisplayName("단어장에서 랜텀 10개 반환 테스트")
    void findRandomDistinctMyWordsTest() {
        // given
        for (int i = 0; i < 20; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            myWordRepository.save(createdMyWord);
        }

        // when
        List<TodayWordDto> foundTodayWordDtoList = myWordRepository.findRandomDistinctMyWords();

        // then
        assertThat(foundTodayWordDtoList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("단어장에서 랜텀 10개 반환 테스트")
    void findRandomDistinctMyWordsDistinctTest() {
        // given
        for (int i = 0; i < 20; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            myWordRepository.save(createdMyWord);
        }

        // when
        List<TodayWordDto> foundTodayWordDtoList = myWordRepository.findRandomDistinctMyWords();

        // then
        assertThat(foundTodayWordDtoList.stream().distinct().count()).isEqualTo(10);
    }

    @Test
    @DisplayName("단어장 페이징 쿼리 테스트 - (성공-단어, 품사 모두 검색)")
    void findMyWordByConditionMyWordTest() {
        // given
        MyWordSearchRequest request = new MyWordSearchRequest("2", "2"); // 2가 들어간 단어와 품사 검색
        Pageable pageRequest = PageRequest.of(0, 10);
        Member createdMember = new Member("홍길동", "password");
        memberRepository.save(createdMember);

        for (int i = 0; i < 20; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            MemberMyWord createdMemberMyWord = new MemberMyWord(createdMember, createdMyWord);

            createdMyWord.addMemberMyWord(createdMemberMyWord);
            myWordRepository.save(createdMyWord);
            memberMyWordRepository.save(createdMemberMyWord);
        }

        // when
        Page<MyWordResponse> foundMyWordResponsePage = myWordRepository
                .findMyWordByConditionMyWord(request, pageRequest, createdMember.getId());

        // then
        assertThat(foundMyWordResponsePage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("단어장 페이징 쿼리 테스트 - (성공-단어만 검색)")
    void findMyWordByConditionMyWordNameSearchTest() {
        // given
        MyWordSearchRequest request = MyWordSearchRequest.builder()
                .name("2")
                .build(); // 2가 들어간 단어와 품사 검색
        Pageable pageRequest = PageRequest.of(0, 10);
        Member createdMember = new Member("홍길동", "password");
        memberRepository.save(createdMember);

        for (int i = 0; i < 20; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            MemberMyWord createdMemberMyWord = new MemberMyWord(createdMember, createdMyWord);

            createdMyWord.addMemberMyWord(createdMemberMyWord);
            myWordRepository.save(createdMyWord);
            memberMyWordRepository.save(createdMemberMyWord);
        }

        // when
        Page<MyWordResponse> foundMyWordResponsePage = myWordRepository
                .findMyWordByConditionMyWord(request, pageRequest, createdMember.getId());

        // then
        assertThat(foundMyWordResponsePage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("단어장 페이징 쿼리 테스트 - (성공-품사만 검색)")
    void findMyWordByConditionMyWordMorphemeSearchTest() {
        // given
        MyWordSearchRequest request = MyWordSearchRequest.builder()
                .morpheme("2")
                .build(); // 2가 들어간 단어와 품사 검색
        Pageable pageRequest = PageRequest.of(0, 10);
        Member createdMember = new Member("홍길동", "password");
        memberRepository.save(createdMember);

        for (int i = 0; i < 20; i++) {
            MyWord createdMyWord = new MyWord("단어" + i, "품사" + i, "의미" + i);
            MemberMyWord createdMemberMyWord = new MemberMyWord(createdMember, createdMyWord);

            createdMyWord.addMemberMyWord(createdMemberMyWord);
            myWordRepository.save(createdMyWord);
            memberMyWordRepository.save(createdMemberMyWord);
        }

        // when
        Page<MyWordResponse> foundMyWordResponsePage = myWordRepository
                .findMyWordByConditionMyWord(request, pageRequest, createdMember.getId());

        // then
        assertThat(foundMyWordResponsePage.getTotalElements()).isEqualTo(2);
    }
}

