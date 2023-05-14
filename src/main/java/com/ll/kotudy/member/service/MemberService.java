package com.ll.kotudy.member.service;

import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.exception.AppException;
import com.ll.kotudy.member.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    public String join(String username, String password) {

        // username 중복 체크
        memberRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,  username + "(은)는 이미 있습니다.");
                });
        Member createdMember = new Member(username, encoder.encode(password));
        memberRepository.save(createdMember);

        return "SUCCESS!!!";
    }
}
