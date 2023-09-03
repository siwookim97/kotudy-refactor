package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.TodayWordDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String morpheme;
    private String mean;

    public TodayWord(TodayWordDto todayWordDto) {
        this.name = todayWordDto.getName();
        this.morpheme = todayWordDto.getMorpheme();
        this.mean = todayWordDto.getMean();
    }
}
