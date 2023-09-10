package com.ll.kotudy.word.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyWordResponse {

    private Long wordId;
    private String name;
    private String morpheme;
    private String mean;

    @QueryProjection
    public MyWordResponse(Long wordId, String name, String morpheme, String mean) {
        this.wordId = wordId;
        this.name = name;
        this.morpheme = morpheme;
        this.mean = mean;
    }
}
