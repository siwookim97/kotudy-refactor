package com.ll.kotudy.member.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TokenHeaderRequest {

    private String token;

    public String getToken() {
        return this.token.split(" ")[1];
    }
}