package com.ll.kotudy.member.dto.reqeust;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {
    private String username;
    private String password;

    public MemberJoinRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
