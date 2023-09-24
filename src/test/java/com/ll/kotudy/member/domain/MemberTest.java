package com.ll.kotudy.member.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("회원 생성 테스트 (성공)")
    void createMemberTest() {
        // given & when & then
        assertDoesNotThrow(() -> Member.builder().username("홍길동").password("password").build());
    }


    @Test
    @DisplayName("회원 점수 추가 테스트")
    void createMemberFailTest() {
        // given
        Member createdMember = Member.builder()
                .username("홍길동")
                .password("password")
                .build();

        // when
        createdMember.plusScore(5);

        // then
        assertThat(createdMember.getScore()).isEqualTo(5);
    }
}