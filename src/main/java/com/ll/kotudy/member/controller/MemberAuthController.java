package com.ll.kotudy.member.controller;

import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import com.ll.kotudy.member.dto.response.JoinResponse;
import com.ll.kotudy.member.dto.response.LoginResponse;
import com.ll.kotudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberAuthController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid MemberJoinRequest request) {
        JoinResponse response = memberService.join(request.getUsername(), request.getPassword());

        // 추 후 ResponseEntity.created(URI).body(response)로 변경예정
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid MemberLoginRequest request) {
        LoginResponse response = memberService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}