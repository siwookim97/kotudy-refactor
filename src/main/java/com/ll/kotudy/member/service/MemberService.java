package com.ll.kotudy.member.service;

import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.dto.response.JoinResponse;
import com.ll.kotudy.member.dto.response.LoginResponse;
import com.ll.kotudy.util.exception.AppException;
import com.ll.kotudy.util.exception.ErrorCode;
import com.ll.kotudy.config.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private static final String MEMBER_JOIN_ALREADY_FAIL = "회원님은 이미 있습니다.";
    private static final String MEMBER_JOIN_SUCCESS = "회원 가입이 완료되었습니다.";
    private static final String MEMBER_LOGIN_DO_NOT_EXIST_FAIL = "회원이(가) 없습니다.";
    private static final String MEMBER_LOGIN_WRONG_PASSWORD_FAIL = "패스워드를 잘못 입력했습니다.";
    private static final String MEMBER_LOGIN_SUCCESS = "로그인이 완료되었습니다.";

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public JoinResponse join(String username, String password) {
        // username 중복 체크
        memberRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,  username + MEMBER_JOIN_ALREADY_FAIL);
                });
        Member createdMember = Member.builder()
                .username(username)
                .password(encoder.encode(password))
                .build();
        memberRepository.save(createdMember);

        return new JoinResponse(MEMBER_JOIN_SUCCESS, createdMember.getId(), createdMember.getUsername());
    }

    public LoginResponse login(String username, String password) {
        // username 없음
        Member selectedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.BODY_BAD_REQUEST, username + MEMBER_LOGIN_DO_NOT_EXIST_FAIL));

        // password 틀림
        if (!encoder.matches(password, selectedMember.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, MEMBER_LOGIN_WRONG_PASSWORD_FAIL);
        }

        // 앞에서 Exception 안났으면 토큰 발행
        return new LoginResponse(MEMBER_LOGIN_SUCCESS, jwtProvider.createToken(selectedMember.getUsername(), selectedMember.getId()));
    }
}

