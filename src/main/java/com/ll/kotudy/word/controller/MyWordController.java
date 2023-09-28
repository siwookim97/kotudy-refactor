package com.ll.kotudy.word.controller;

import com.ll.kotudy.config.auth.JwtProvider;
import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.request.MyWordSearchRequest;
import com.ll.kotudy.word.dto.response.MyWordAddResponse;
import com.ll.kotudy.word.dto.response.MyWordDeleteResponse;
import com.ll.kotudy.word.dto.response.MyWordSearchResponse;
import com.ll.kotudy.word.service.MyWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/myWord")
public class MyWordController {

    private final MyWordService myWordService;
    private final JwtProvider jwtProvider;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public EntityModel<MyWordAddResponse> addMyWord(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @RequestBody @Valid MyWordAddRequest request) {

        MyWordAddResponse response = myWordService.add(request, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return toModelMyWordAddResponse(tokenHeaderRequest, request, response);
    }

    @DeleteMapping("/{myWordId}")
    @PreAuthorize("isAuthenticated()")
    public EntityModel<MyWordDeleteResponse> deleteMyWord(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @PathVariable("myWordId") Long myWordId) {

        MyWordDeleteResponse response = myWordService.delete(myWordId, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return toModelMyWordDeleterResponse(tokenHeaderRequest, myWordId, response);
    }

    private EntityModel<MyWordAddResponse> toModelMyWordAddResponse(
            TokenHeaderRequest tokenHeaderRequest,
            MyWordAddRequest request, MyWordAddResponse response) {

        return EntityModel.of(response,
                linkTo(methodOn(MyWordController.class)
                        .addMyWord(tokenHeaderRequest, request))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(MyWordController.class)
                        .deleteMyWord(tokenHeaderRequest, response.getData().getWordId()))
                        .withRel("delete-myWord")
                        .withType("DELETE"),
                linkTo(methodOn(MyWordController.class)
                        .searchMyWord(tokenHeaderRequest, new MyWordSearchRequest("name", "morpheme"), 0, 10))
                        .withRel("search-myWord")
                        .withType("GET")
        );
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public EntityModel<MyWordSearchResponse> searchMyWord(
            @RequestHeader("Authorization") TokenHeaderRequest tokenHeaderRequest,
            @RequestBody @Valid MyWordSearchRequest request,
            @RequestParam("page") Integer page,
            @RequestParam("count") int count) {

        Pageable pageRequest = PageRequest.of(page, count);
        MyWordSearchResponse response = myWordService.searchByPagenation(
                request, pageRequest, jwtProvider.getId(tokenHeaderRequest.getToken()));

        return toModelMyWordSearchResponse(tokenHeaderRequest, request, page, count, response);
    }

    private EntityModel<MyWordDeleteResponse> toModelMyWordDeleterResponse(
            TokenHeaderRequest tokenHeaderRequest,
            Long myWordId,
            MyWordDeleteResponse response) {

        return EntityModel.of(response,
                linkTo(methodOn(MyWordController.class)
                        .deleteMyWord(tokenHeaderRequest, myWordId))
                        .withSelfRel()
                        .withType("DELETE"),
                linkTo(methodOn(MyWordController.class)
                        .searchMyWord(tokenHeaderRequest, new MyWordSearchRequest("name", "morpheme"), 0, 10))
                        .withRel("search-myWord")
                        .withType("GET")
        );
    }

    private EntityModel<MyWordSearchResponse> toModelMyWordSearchResponse(
            TokenHeaderRequest tokenHeaderRequest,
            MyWordSearchRequest request, Integer page,
            int count, MyWordSearchResponse response) {

        return EntityModel.of(response,
                linkTo(methodOn(MyWordController.class)
                        .searchMyWord(tokenHeaderRequest, request, page, count))
                        .withSelfRel()
                        .withType("GET")
        );
    }
}
