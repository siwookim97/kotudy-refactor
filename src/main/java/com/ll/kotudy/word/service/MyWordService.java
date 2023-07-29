package com.ll.kotudy.word.service;

import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.util.exception.ErrorCode;
import com.ll.kotudy.word.domain.MemberMyWord;
import com.ll.kotudy.word.domain.MemberMyWordRepository;
import com.ll.kotudy.word.domain.MyWord;
import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.response.MyWordAddResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyWordService {

    private final MyWordRepository myWordRepository;
    private final MemberMyWordRepository memberMyWordRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MyWordAddResponse add(MyWordAddRequest request, Long loginId) {
        Member findMember = getFindMember(loginId);
        MyWord findMyWord = getFindMyWord(request);

        if (isExists(loginId, findMyWord.getId())) {
            return new MyWordAddResponse(findMyWord.getName() + "님의 단어 추가가 실패하였습니다.",
                    findMyWord.getName(),
                    findMyWord.getMorpheme(),
                    findMyWord.getMean());
        }

        findMyWord.plusCount();
        MemberMyWord createdMemberMyWord = MemberMyWord.builder()
                .member(findMember)
                .myWord(findMyWord)
                .build();
        memberMyWordRepository.save(createdMemberMyWord);

        return new MyWordAddResponse(findMyWord.getName() + "님의 단어 추가가 완료되었습니다.",
                findMyWord.getName(),
                findMyWord.getMorpheme(),
                findMyWord.getMean());
    }

    private boolean isExists(Long loginId, Long myWordId) {
        return memberMyWordRepository.existsByMemberIdAndMyWordId(loginId, myWordId);
    }

    private Member getFindMember(Long loginId) {
        return memberRepository.findById(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, loginId + " 번호의 회원이 없습니다."));
    }

    private MyWord getFindMyWord(MyWordAddRequest request) {
        return myWordRepository
                .findByNameAndMorphemeAndMean(request.getName(), request.getMorpheme(), request.getMean())
                .orElseGet(() -> {
                    MyWord newWord = MyWord.builder()
                            .name(request.getName())
                            .morpheme(request.getMorpheme())
                            .mean(request.getMean())
                            .build();
                    return myWordRepository.save(newWord);
                });
    }
}
