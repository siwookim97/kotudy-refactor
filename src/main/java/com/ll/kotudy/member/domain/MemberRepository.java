package com.ll.kotudy.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findFirstByUsernameAndPassword(String username, String password);
    Optional<Member> findByUsername(String username);
}
