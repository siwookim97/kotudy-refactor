package com.ll.kotudy.word.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyWordResponse {

    private Long wordId;
    private String name;
    private String morpheme;
    private String mean;

    @Builder
    public MyWordResponse(Long wordId, String name, String morpheme, String mean) {
        this.wordId = wordId;
        this.name = name;
        this.morpheme = morpheme;
        this.mean = mean;
    }
}
