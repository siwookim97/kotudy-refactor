package com.ll.kotudy.word.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class MyWordSearchResponse {

    private String msg;
    private Page<MyWordResponse> datum;
}
