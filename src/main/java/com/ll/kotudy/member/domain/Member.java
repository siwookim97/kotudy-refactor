package com.ll.kotudy.member.domain;

import com.ll.kotudy.util.baseEntity.BaseEntity;
import com.ll.kotudy.word.domain.MemberMyWord;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "members"
        , indexes = {
        @Index(name = "idx__username", columnList = "username", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ColumnDefault("0")
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberMyWord> memberMyWords = new ArrayList<>();

    public void addMemberMyWord(MemberMyWord memberMyWord) {
        this.getMemberMyWords().add(memberMyWord);
        if (memberMyWord.getMember() != this) {
            memberMyWord.setMember(this);
        }
    }

    @Builder
    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = MemberRole.ROLE_USER;
    }

    public void plusScore(int quizScore) {
        score += quizScore;
    }
}

