package com.ll.kotudy.word.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMyWordRepository extends JpaRepository<MemberMyWord, Long> {

    boolean existsByMemberIdAndMyWordId(Long memberId, Long myWordId);

    void deleteByMemberIdAndMyWordId(Long MemberId, Long myWordId);
}
