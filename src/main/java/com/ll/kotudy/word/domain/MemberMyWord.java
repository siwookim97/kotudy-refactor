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

    @Builder
    public MemberMyWord(Member member, MyWord myWord) {
        setUpMember(member);
        setUpMyWord(myWord);
    }

    private void setUpMember(Member member) {
        if (this.member != null) {
            this.member.getMemberMyWords().remove(this);
        }
        this.member = member;
        member.getMemberMyWords().add(this);
    }

    private void setUpMyWord(MyWord myWord) {
        if (this.myWord != null) {
            this.myWord.getMemberMyWords().remove(this);
        }
        this.myWord = myWord;
        myWord.getMemberMyWords().add(this);
    }
}
