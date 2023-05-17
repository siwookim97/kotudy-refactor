package com.ll.kotudy.member.service;

import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.dto.response.JoinResponse;
import com.ll.kotudy.member.dto.response.LoginResponse;
import com.ll.kotudy.member.exception.AppException;
import com.ll.kotudy.member.exception.ErrorCode;
import com.ll.kotudy.config.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public JoinResponse join(String username, String password) {
        // username 중복 체크
        memberRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,  username + "(은)는 이미 있습니다.");
                });
        Member createdMember = new Member(username, encoder.encode(password));
        memberRepository.save(createdMember);

        return new JoinResponse("회원 가입이 완료되었습니다.", createdMember.getId(), createdMember.getUsername());
    }

    public LoginResponse login(String username, String password) {
        // username 없음
        Member selectedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, username + "이 없습니다."));

        // password 틀림
        if (!encoder.matches(password, selectedMember.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        // 앞에서 Exception 안났으면 토큰 발행
        return new LoginResponse("로그인이 완료되었습니다.", jwtProvider.createToken(selectedMember.getUsername()));
    }
}
