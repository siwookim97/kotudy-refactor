package com.ll.kotudy.word.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRankingDto {

    private int ranking;
    private String username;
    private int score;

    public MemberRankingDto (String username, int score) {
        this.username = username;
        this.score = score;
    }
}
