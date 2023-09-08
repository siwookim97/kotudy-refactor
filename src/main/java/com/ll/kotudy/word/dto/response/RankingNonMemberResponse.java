package com.ll.kotudy.word.dto.response;

import com.ll.kotudy.word.dto.MemberRankingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class RankingNonMemberResponse {

    private String msg;
    private List<MemberRankingDto> topMemberRanking;
}
