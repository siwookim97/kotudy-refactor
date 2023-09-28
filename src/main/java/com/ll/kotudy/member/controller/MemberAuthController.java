package com.ll.kotudy.member.controller;

import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.member.dto.response.JoinResponse;
import com.ll.kotudy.member.dto.response.LoginResponse;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.word.controller.MyWordController;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberAuthController {

    private final MemberService memberService;

    @PostMapping("/join")
    public EntityModel<JoinResponse> join(@RequestBody @Valid MemberJoinRequest request) {
        JoinResponse response = memberService.join(request.getUsername(), request.getPassword());

        return toModelJoinResponse(response);
    }

    @PostMapping("/login")
    public EntityModel<LoginResponse> login(@RequestBody @Valid MemberLoginRequest request) {
        LoginResponse response = memberService.login(request.getUsername(), request.getPassword());

        return toModelLoginResponse(request, response);
    }

    private EntityModel<JoinResponse> toModelJoinResponse(JoinResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(MemberAuthController.class)
                        .join(new MemberJoinRequest(response.getUsername(), response.getPassword())))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(MemberAuthController.class)
                        .login(new MemberLoginRequest(response.getUsername(), response.getPassword())))
                        .withRel("login")
                        .withType("POST")
        );
    }

    private EntityModel<LoginResponse> toModelLoginResponse(MemberLoginRequest request, LoginResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(MemberAuthController.class)
                        .login(new MemberLoginRequest(request.getUsername(), request.getPassword())))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(MyWordController.class)
                        .searchMyWord(new TokenHeaderRequest(response.getAccessToken()), new MyWordSearchRequest("name", "morpheme"), 0, 10))
                        .withRel("login")
                        .withType("POST")
        );
    }
}