package com.ll.kotudy.member.domain;

import com.ll.kotudy.word.domain.MemberMyWord;
import com.ll.kotudy.word.domain.MemberMyWordRepository;
import com.ll.kotudy.word.domain.MyWord;
import com.ll.kotudy.word.domain.MyWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    MyWordRepository myWordRepository;

    @Autowired
    MemberMyWordRepository memberMyWordRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member member_A = new Member("김시우", "1234");
        Member member_B = new Member("김민재", "4321");
        memberRepository.save(member_A);
        memberRepository.save(member_B);

        MyWord myWord_A = new MyWord("달리다", "동사", "뜀박질을 하다");
        MyWord myWord_B = new MyWord("수영하다", "동사", "수영을 하다");
        MyWord myWord_C = new MyWord("책", "명사", "종이로 엮은 문서");
        myWordRepository.save(myWord_A);
        myWordRepository.save(myWord_B);
        myWordRepository.save(myWord_C);

        MemberMyWord memberMyWord_AA = new MemberMyWord(member_A, myWord_A);
        member_A.addMemberMyWord(memberMyWord_AA);
        myWord_A.addMemberMyWord(memberMyWord_AA);
        MemberMyWord memberMyWord_AB = new MemberMyWord(member_A, myWord_B);
        member_A.addMemberMyWord(memberMyWord_AB);
        myWord_B.addMemberMyWord(memberMyWord_AB);

        MemberMyWord memberMyWord_AC = new MemberMyWord(member_A, myWord_C);
        member_A.addMemberMyWord(memberMyWord_AC);
        myWord_C.addMemberMyWord(memberMyWord_AC);

        MemberMyWord memberMyWord_BA = new MemberMyWord(member_B, myWord_A);
        member_B.addMemberMyWord(memberMyWord_BA);
        myWord_A.addMemberMyWord(memberMyWord_BA);

        MemberMyWord memberMyWord_BB = new MemberMyWord(member_B, myWord_B);
        member_B.addMemberMyWord(memberMyWord_BB);
        myWord_B.addMemberMyWord(memberMyWord_BB);

        memberMyWordRepository.save(memberMyWord_AA);
        memberMyWordRepository.save(memberMyWord_AB);
        memberMyWordRepository.save(memberMyWord_AC);
        memberMyWordRepository.save(memberMyWord_BA);
        memberMyWordRepository.save(memberMyWord_BB);
    }

    @Test
    @Rollback(value = false)
    @DisplayName("member 테이블에서 내 단어장 목록 띄워주는 쿼리")
    void findFirstByNameAndPassword() {
        Member findMember_1 = memberRepository.findFirstByUsernameAndPassword("김시우", "1234").orElse(null);
        Member findMember_2 = memberRepository.findFirstByUsernameAndPassword("김민재", "4321").orElse(null);

        assertThat(findMember_1.getMemberMyWords().size()).isEqualTo(3);
        assertThat(findMember_2.getMemberMyWords().size()).isEqualTo(2);
    }

}