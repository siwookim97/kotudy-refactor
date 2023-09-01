package com.ll.kotudy.word.dto.response;

import com.ll.kotudy.word.dto.QuizForm;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuizResultResponse {

    private String msg;
    private int raking;
    private int score;
}
