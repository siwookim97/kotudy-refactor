package com.ll.kotudy.word.dto.response;

import com.ll.kotudy.word.dto.TodayWordDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class TodayWordResponse {

    private String msg;
    private List<TodayWordDto> datum;
}
