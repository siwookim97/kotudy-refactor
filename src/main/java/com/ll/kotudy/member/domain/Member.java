package com.ll.kotudy.member.domain;

import com.ll.kotudy.util.baseEntity.BaseEntity;
import com.ll.kotudy.word.domain.MemberMyWord;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String password;
    private String image;

    @OneToMany(mappedBy = "member")
    private List<MemberMyWord> memberMyWords = new ArrayList<>();

    public void addMemberMyWord(MemberMyWord memberMyWord) {
        this.getMemberMyWords().add(memberMyWord);
        if (memberMyWord.getMember() != this) {
            memberMyWord.setMember(this);
        }
    }

    public Member(String name, String username, String password, String image) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.image = image;
    }
}
