package com.ll.kotudy.word.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordSenceDto implements Serializable {

    private int senseOrder;
    private String definition;
}
