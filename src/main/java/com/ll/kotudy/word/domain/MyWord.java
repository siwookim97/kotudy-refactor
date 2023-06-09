package com.ll.kotudy.word.domain;

import com.ll.kotudy.util.baseEntity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String morpheme;
    private String mean;

    @ColumnDefault("0")
    private long count; // 추가 된 횟수

    @OneToMany(mappedBy = "myWord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberMyWord> membmerMyWords = new ArrayList<>();

    public void addMemberMyWord(MemberMyWord memberMyWord) {
        this.getMembmerMyWords().add(memberMyWord);
        if (memberMyWord.getMyWord() != null) {
            memberMyWord.setMyWord(this);
        }
    }

    public MyWord(String name, String morpheme, String mean) {
        this.name = name;
        this.morpheme = morpheme;
        this.mean = mean;
    }

    public void plusCount() {
        this.count++;
    }
}
