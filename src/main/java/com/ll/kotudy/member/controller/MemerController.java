package com.ll.kotudy.member.controller;

import com.ll.kotudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MemerController {

    private final MemberService memberService;

}
