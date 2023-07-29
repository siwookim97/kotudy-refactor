package com.ll.kotudy.word.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyWordAddRequest {

    @NotEmpty(message = "단어의 이름은 공백이 불가합니다.")
    private String name;

    @NotEmpty(message = "단어의 품사는 공백이 불가합니다.")
    private String morpheme;

    @NotEmpty(message = "단어의 의미는 공백이 불가합니다.")
    private String mean;
}
