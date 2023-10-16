package com.ll.kotudy.member.dto.response;

import com.ll.kotudy.word.domain.MemberMyWord;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchMemberResponse {

    private String msg;
    private Long id;
    private String username;
    private List<MemberMyWord> memberMyWordList;
}