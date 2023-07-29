package com.ll.kotudy.word.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyWordAddResponse {

    private String msg;
    private String name;
    private String mopheme;
    private String mean;
}
