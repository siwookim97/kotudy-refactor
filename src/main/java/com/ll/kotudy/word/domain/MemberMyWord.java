package com.ll.kotudy.word.domain;

import com.ll.kotudy.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_word_id")
    @Setter
    private MyWord myWord;

    public MemberMyWord(Member member, MyWord myWord) {
        this.member = member;
        this.myWord = myWord;
    }
}
