package com.ll.kotudy.member.domain;

import com.ll.kotudy.word.dto.MemberRankingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findFirstByUsernameAndPassword(String username, String password);
    Optional<Member> findByUsername(String username);

    @Query("SELECT COUNT(m) + 1 FROM Member m WHERE m.score > (SELECT m2.score FROM Member m2 WHERE m2.id = :memberId)")
    int findRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT NEW com.ll.kotudy.word.dto.MemberRankingDto(m.username, m.score) " +
            "FROM Member m " +
            "ORDER BY m.score DESC")
    List<MemberRankingDto> findTop10MembersByScoreDesc();
}
