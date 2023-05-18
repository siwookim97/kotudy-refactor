package com.ll.kotudy.member.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequest {

    @NotEmpty(message = "회원 이름은 공백이 불가합니다.")
    private String username;

    @NotEmpty(message = "회원 비밀번호는 공백이 불가합니다.")
    private String password;
}
