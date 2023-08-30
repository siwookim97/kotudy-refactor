package com.ll.kotudy.word.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizResponse {

    private String msg;
    private QuizFormResponse data;
}
