package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.dto.TodayWordDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "today_words")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String morpheme;

    @Column(nullable = false)
    private String mean;

    public TodayWord(TodayWordDto todayWordDto) {
        this.name = todayWordDto.getName();
        this.morpheme = todayWordDto.getMorpheme();
        this.mean = todayWordDto.getMean();
    }
}
