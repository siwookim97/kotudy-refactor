package com.ll.kotudy.word.domain;

import com.ll.kotudy.util.baseEntity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "my_words")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String morpheme;

    @Column(nullable = false)
    private String mean;

    @ColumnDefault("0")
    private long count; // 추가 된 횟수

    @OneToMany(mappedBy = "myWord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberMyWord> memberMyWords = new ArrayList<>();

    public void addMemberMyWord(MemberMyWord memberMyWord) {
        this.getMemberMyWords().add(memberMyWord);
        if (memberMyWord.getMyWord() != null) {
            memberMyWord.setMyWord(this);
        }
    }

    @Builder
    public MyWord(String name, String morpheme, String mean) {
        this.name = name;
        this.morpheme = morpheme;
        this.mean = mean;
    }

    public void plusCount() {
        this.count++;
    }

    public void minusCount() {
        if (this.count != 0) {
            this.count--;
        }
    }
}
