package com.ll.kotudy.word.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWordSearchRequest {

    private String name;
    private String morpheme;
}
