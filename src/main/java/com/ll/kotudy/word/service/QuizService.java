package com.ll.kotudy.word.service;

import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.util.exception.ErrorCode;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.dto.MemberRankingDto;
import com.ll.kotudy.word.dto.QuizWordDto;
import com.ll.kotudy.word.dto.QuizForm;
import com.ll.kotudy.word.dto.request.QuizResultRequest;
import com.ll.kotudy.word.dto.response.QuizResponse;
import com.ll.kotudy.word.dto.response.QuizResultResponse;
import com.ll.kotudy.word.dto.response.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private final MyWordRepository myWordRepository;
    private final MemberRepository memberRepository;

    public QuizResponse createForm() {
        List<QuizWordDto> quizWordDtoList = myWordRepository.findMyWordDistinctRandomForQuiz();
        checkEnoughMyWordToCreateQuiz(quizWordDtoList);
        List<QuizForm> quizFormList = new ArrayList<>();

        quizFormList = IntStream.range(0, quizWordDtoList.size() / 4)
                .mapToObj(i -> createOneQuizForm(quizWordDtoList.subList(i * 4, Math.min((i + 1) * 4, quizWordDtoList.size()))))
                .collect(Collectors.toList());

        return new QuizResponse("나만의 단어장을 기반으로 생성된 퀴즈입니다.", quizFormList);
    }

    private void checkEnoughMyWordToCreateQuiz(List<QuizWordDto> quizWordDtoList) {
        if (quizWordDtoList.size() < 40) {
            throw new AppException(ErrorCode.NOT_ENOUGH_MYWORD_FOR_CREATE_QUIZ, "퀴즈를 만들기에 충분한 단어가 없습니다.");
        }
    }

    private QuizForm createOneQuizForm (List<QuizWordDto> subQuizList) {
        QuizForm createdQuizForm = new QuizForm();
        String answerWord = subQuizList.get(0).getName();

        List<String> choices = shuffleChoices(subQuizList);

        createdQuizForm.setQuestion(subQuizList.get(0).getMean());
        createdQuizForm.setAnswerIndex(choices.indexOf(answerWord) + 1);
        createdQuizForm.setChoices(choices);

        return createdQuizForm;
    }

    private List<String> shuffleChoices(List<QuizWordDto> subQuizList) {
        List<String> choices = subQuizList.stream()
                .map(QuizWordDto::getName)
                .collect(Collectors.toList());

        Collections.shuffle(choices);

        return choices;
    }

    @Transactional
    public QuizResultResponse applyScore(QuizResultRequest request, Long loginId) {
        Member findMember = getFindMember(loginId);
        addScore(request, findMember);
        int rank = getRankByMemberId(loginId);

        return new QuizResultResponse("퀴즈 결과가 반영되었습니다.", rank, findMember.getScore());
    }

    private void addScore(QuizResultRequest request, Member findMember) {
        findMember.plusScore(request.getScore());
        memberRepository.save(findMember);
    }

    private Member getFindMember(Long loginId) {
        return memberRepository.findById(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, loginId + " 번호의 회원이 없습니다."));
    }
    
    private int getRankByMemberId(Long loginId) {
        return memberRepository.findRankByMemberId(loginId);
    }

    public RankingResponse getQuizRaking(Long loginId) {
        Member findMember = getFindMember(loginId);
        int rank = getRankByMemberId(loginId);
        String username = findMember.getUsername();
        int score = findMember.getScore();

        List<MemberRankingDto> memberRankingDtoList = setMemberRankingDtoList();

        return new RankingResponse("퀴즈 랭킹 결과입니다.", rank, username, score, memberRankingDtoList);
    }

    private List<MemberRankingDto> setMemberRankingDtoList() {
        List<MemberRankingDto> memberRankingDtoList = memberRepository.findTop10MembersByScoreDesc();
        memberRankingDtoList.forEach(dto -> dto.setRanking(memberRankingDtoList.indexOf(dto) + 1));

        return memberRankingDtoList;
    }
}
