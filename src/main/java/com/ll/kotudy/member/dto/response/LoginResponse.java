package com.ll.kotudy.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String msg;
    private String accessToken;
    // private String refreshToken;
}
