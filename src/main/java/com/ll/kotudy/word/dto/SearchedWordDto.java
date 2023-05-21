package com.ll.kotudy.word.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchedWordDto {

    private int targetCode;
    private String word;
    private String pronunciation;
    private String pos;
    private List<WordSenceDto> wordSenceList;

}
