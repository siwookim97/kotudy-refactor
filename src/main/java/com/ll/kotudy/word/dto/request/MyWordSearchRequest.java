package com.ll.kotudy.word.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyWordSearchRequest {

    private String name;
    private Integer ageGoe;
    private Integer ageLoe;
}
