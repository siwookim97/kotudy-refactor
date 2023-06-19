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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role; // ROLE_ADMIN, ROLE_USER, ROLE_GUEST

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberMyWord> memberMyWords = new ArrayList<>();

    public void addMemberMyWord(MemberMyWord memberMyWord) {
        this.getMemberMyWords().add(memberMyWord);
        if (memberMyWord.getMember() != this) {
            memberMyWord.setMember(this);
        }
    }

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = MemberRole.ROLE_USER;
    }
}
