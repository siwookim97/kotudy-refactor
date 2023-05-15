package com.ll.kotudy.member.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberLoginRequest {

    private String username;
    private String password;
}
