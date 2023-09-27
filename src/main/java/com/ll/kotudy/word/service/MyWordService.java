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
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordAddResponse;
import com.ll.kotudy.word.dto.response.MyWordDeleteResponse;
import com.ll.kotudy.word.dto.response.MyWordResponse;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyWordService {

    private static final String DO_NOT_HAVE_MEMBER_ID = " 번호의 회원이 없습니다.";
    private static final String DO_NOT_HAVE_MYWORD_ID = " 번호의 단어가 없습니다.";
    private static final String MYWORD_ADD_SUCCESS = " 단어 추가가 성공하였습니다.";
    private static final String MYWORD_ADD_ALREADY_FAIL = " 단어 추가가 실패하였습니다.(이미 단어장에 존재)";
    private static final String MYWORD_DELETE_DO_NOT_SEARCH_FAIL = "번의 단어 삭제가 실패하였습니다. (나만의 단어장에 존재하지 않는 단어입니다)";
    private static final String MYWORD_DELETE_SUCCESS = "번의 단어 삭제를 성공하였습니다.";
    private static final String MYWROD_SEARCH_SUCCESS = "나만의 단어 검색 결과는 다음과 같습니다.";

    private final MyWordRepository myWordRepository;
    private final MemberMyWordRepository memberMyWordRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MyWordAddResponse add(MyWordAddRequest request, Long loginId) {
        Member findMember = getFindMember(loginId);
        MyWord findMyWord = getFindMyWord(request);

        if (isExists(loginId, findMyWord.getId())) {
            return createAddMethodReponseFail(findMyWord);
        }

        findMyWord.plusCount();
        MemberMyWord createdMemberMyWord = MemberMyWord.builder()
                .member(findMember)
                .myWord(findMyWord)
                .build();
        memberMyWordRepository.save(createdMemberMyWord);

        return createAddMethodReponseScuccess(findMyWord);
    }

    private boolean isExists(Long loginId, Long myWordId) {
        return memberMyWordRepository.existsByMemberIdAndMyWordId(loginId, myWordId);
    }

    private Member getFindMember(Long loginId) {
        return memberRepository.findById(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, loginId + DO_NOT_HAVE_MEMBER_ID));
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

    private MyWordAddResponse createAddMethodReponseFail(MyWord responseEntity) {
        MyWordResponse item = new MyWordResponse(
                responseEntity.getId(),
                responseEntity.getName(),
                responseEntity.getMorpheme(),
                responseEntity.getMean()
        );

        return new MyWordAddResponse(responseEntity.getName() + MYWORD_ADD_ALREADY_FAIL, item);
    }

    private MyWordAddResponse createAddMethodReponseScuccess(MyWord responseEntity) {
        MyWordResponse item = new MyWordResponse(
                responseEntity.getId(),
                responseEntity.getName(),
                responseEntity.getMorpheme(),
                responseEntity.getMean()
        );

        return new MyWordAddResponse(responseEntity.getName() + MYWORD_ADD_SUCCESS, item);
    }

    @Transactional
    public MyWordDeleteResponse delete(Long myWordId, Long loginId) {
        if (!isExists(loginId, myWordId)) {
            return createDeleteMethodReponseFailByNotExist(myWordId);
        }
        
        MyWord findMyWord = getFindMyWord(myWordId);
        findMyWord.minusCount();
        memberMyWordRepository.deleteByMemberIdAndMyWordId(loginId, myWordId);
        deleteIfMyWordEqualToZero(findMyWord);

        return createDeleteMethodReponseSuccess(findMyWord);
    }

    private MyWord getFindMyWord(Long myWordId) {
        return myWordRepository.findById(myWordId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, myWordId + DO_NOT_HAVE_MYWORD_ID));
    }

    private void deleteIfMyWordEqualToZero(MyWord findMyWord) {
        if (findMyWord.getCount() == 0) {
            myWordRepository.delete(findMyWord);
        }
    }

    private MyWordDeleteResponse createDeleteMethodReponseFailByNotExist(Long myWordId) {
        MyWordResponse item = new MyWordResponse(
                myWordId, null, null, null
        );

        return new MyWordDeleteResponse(
                myWordId + MYWORD_DELETE_DO_NOT_SEARCH_FAIL, item);
    }

    private MyWordDeleteResponse createDeleteMethodReponseSuccess(MyWord responseEntity) {
        MyWordResponse item = new MyWordResponse(
                responseEntity.getId(),
                responseEntity.getName(),
                responseEntity.getMorpheme(),
                responseEntity.getMean()
        );

        return new MyWordDeleteResponse(responseEntity.getId() + MYWORD_DELETE_SUCCESS, item);
    }

    public MyWordSearchResponse searchByPagenation(MyWordSearchRequest condition, Pageable pageable, Long memberId) {
        Page<MyWordResponse> datum = myWordRepository.findMyWordByConditionMyWord(condition, pageable, memberId);

        return new MyWordSearchResponse(MYWROD_SEARCH_SUCCESS, datum);
    }
}