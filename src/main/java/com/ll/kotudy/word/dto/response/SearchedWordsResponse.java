package com.ll.kotudy.word.dto.response;

import com.ll.kotudy.word.dto.SearchedWordDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchedWordsResponse implements Serializable {

    private String msg;
    private List<SearchedWordDto> data;
}
