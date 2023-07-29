package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.domain.MemberRepository;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.response.MyWordAddResponse;
import com.ll.kotudy.word.dto.response.MyWordDeleteResponse;
import com.ll.kotudy.word.service.MyWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/myWord")
public class MyWordController {

    private final MyWordService myWordService;
    private final JwtProvider jwtProvider;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyWordAddResponse> addMyWord(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @RequestBody @Valid MyWordAddRequest request) {

        MyWordAddResponse response = myWordService.add(request, jwtProvider.getId(tokenHeaderRequest.getToken()));

        // 추 후 ResponseEntity.created(URI).body(response)로 변경예정
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{myWordId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyWordDeleteResponse> deleteMyWord(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @PathVariable("myWordId") Long myWordId) {

        MyWordDeleteResponse response = myWordService.delete(myWordId, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return ResponseEntity.ok(response);
    }
}
