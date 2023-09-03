package com.ll.kotudy.word.dto;

import com.ll.kotudy.word.domain.TodayWord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TodayWordDto {

    private String name;
    private String morpheme;
    private String mean;

    public TodayWordDto(TodayWord todayWord) {
        this.name = todayWord.getName();
        this.morpheme = todayWord.getMorpheme();
        this.mean = todayWord.getMean();
    }
}
