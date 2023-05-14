package com.ll.kotudy.member.controller;

import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MemerController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest request) {
        memberService.join(request.getUsername(), request.getPassword());

        return ResponseEntity.ok().body("회원가입이 성공 했습니다.");
    }
}
