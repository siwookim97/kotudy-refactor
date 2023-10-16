package com.ll.kotudy.member.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.MemberJoinRequest;
import com.ll.kotudy.member.dto.reqeust.MemberLoginRequest;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.member.dto.response.JoinResponse;
import com.ll.kotudy.member.dto.response.LoginResponse;
import com.ll.kotudy.member.dto.response.SearchMemberResponse;
import com.ll.kotudy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<EntityModel<JoinResponse>> join(@RequestBody @Valid MemberJoinRequest request) {
        JoinResponse response = memberService.join(request.getUsername(), request.getPassword());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(uri).body(toModelJoinResponse(response));
    }

    @GetMapping("/{id}")
    public EntityModel<SearchMemberResponse> searchById(@PathVariable Long id) {
        SearchMemberResponse response = memberService.search(id);

        return toModelSearchMemberResponseId(response);
    }

    @GetMapping
    public EntityModel<SearchMemberResponse> search(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest) {

        SearchMemberResponse response = memberService.search(jwtProvider.getId(tokenHeaderRequest.getToken()));

        return EntityModel.of(response);
    }

    @PostMapping("/login")
    public EntityModel<LoginResponse> login(@RequestBody @Valid MemberLoginRequest request) {
        LoginResponse response = memberService.login(request.getUsername(), request.getPassword());

        return EntityModel.of(response);
    }

    private EntityModel<JoinResponse> toModelJoinResponse(JoinResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(MemberController.class)
                        .join(new MemberJoinRequest(response.getUsername(), response.getPassword())))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(MemberController.class)
                        .login(new MemberLoginRequest(response.getUsername(), response.getPassword())))
                        .withRel("login")
                        .withType("POST")
        );
    }

    private EntityModel<SearchMemberResponse> toModelSearchMemberResponseId(SearchMemberResponse response) {
        return EntityModel.of(response, linkTo(methodOn(MemberController.class)
                .searchById(response.getId())).withSelfRel()
                .withType("GET"));
    }
}