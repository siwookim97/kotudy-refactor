package com.ll.kotudy.word.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class QuizWordDto {

    private String name;
    private String morpheme;
    private String mean;
}
