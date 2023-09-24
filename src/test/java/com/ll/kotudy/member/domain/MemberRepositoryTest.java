package com.ll.kotudy.member.domain;

import com.ll.kotudy.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(QuerydslConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 테스트")
    void saveMember() {
        // given
        Member createdMember = new Member("홍길동", "password");

        // when
        Member savedMember = memberRepository.save(createdMember);

        // then
        assertAll(
                () -> assertThat(savedMember.getUsername()).isEqualTo("홍길동"),
                () -> assertThat(savedMember.getPassword()).isEqualTo("password"),
                () -> assertThat(savedMember.getScore()).isEqualTo(0),
                () -> assertThat(savedMember.getRole()).isEqualTo(MemberRole.ROLE_USER)
        );
    }

    @Test
    @DisplayName("username 값으로 회원의 존재 여부 테스트")
    void existsMemberByUsername() {
        // given
        Member createdMember = new Member("홍길동", "password");
        memberRepository.save(createdMember);

        // when
        Optional<Member> foundMember1 = memberRepository.findByUsername("홍길동");
        Optional<Member> foundMember2 = memberRepository.findByUsername("김판서");

        // then
        assertAll(
                () -> assertThat(foundMember1.isPresent()).isTrue(),
                () -> assertThat(foundMember2.isPresent()).isFalse()
        );
    }

    @Test
    @DisplayName("랭크 조회 테스트")
    void isEncrytPassword() {
        // given
        Member createdMember1 = new Member("홍길동1", "password");
        Member createdMember2 = new Member("김판서1", "password");
        memberRepository.save(createdMember1);
        memberRepository.save(createdMember2);

        // when
        createdMember2.plusScore(5);
        int foundRank = memberRepository.findRankByMemberId(createdMember1.getId());

        // then
        assertThat(foundRank).isEqualTo(2);
    }
}