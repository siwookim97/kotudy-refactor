package com.ll.kotudy.word.service;

import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.dto.response.QuizFormResponse;
import com.ll.kotudy.word.dto.response.QuizResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private final MyWordRepository myWordRepository;
    private final MemberRepository memberRepository;

    public QuizResponse createForm() {

        return new QuizResponse("퀴즈생성?", new QuizFormResponse());
    }
}
