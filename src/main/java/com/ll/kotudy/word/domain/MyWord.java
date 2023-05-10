package com.ll.kotudy.word.domain;

import com.ll.kotudy.util.baseEntity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

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
    private long count;

    public MyWord(String name, String morpheme, String mean) {
        this.name = name;
        this.morpheme = morpheme;
        this.mean = mean;
    }

    public void plusCount() {
        this.count++;
    }
}
