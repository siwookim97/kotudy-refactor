package com.ll.kotudy.word.controller;

import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.response.TodayWordResponse;
import com.ll.kotudy.word.service.TodayWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/todayWord")
public class TodayWordController {

    private final TodayWordService todayWordService;

    @GetMapping
    public EntityModel<TodayWordResponse> getTodayWordList() {
        TodayWordResponse response = todayWordService.getTodayWordList();

        return toModelTodayWordResponse(response);
    }

    private EntityModel<TodayWordResponse> toModelTodayWordResponse(TodayWordResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(TodayWordController.class)
                        .getTodayWordList())
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(MyWordController.class)
                        .addMyWord(new TokenHeaderRequest("Authorization Token"), new MyWordAddRequest("word", "morpheme", "mean")))
                        .withRel("add-myWord")
                        .withType("POST")
        );
    }
}
