package com.ll.kotudy.word.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchedWordDto implements Serializable {

    private int targetCode;
    private String word;
    private String pronunciation;
    private String pos;
    private List<WordSenceDto> wordSenceList;

}
