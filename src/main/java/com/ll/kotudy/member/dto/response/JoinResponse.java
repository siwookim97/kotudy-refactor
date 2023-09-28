package com.ll.kotudy.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinResponse {

    private String msg;
    private Long id;
    private String username;
    private String password;
}
