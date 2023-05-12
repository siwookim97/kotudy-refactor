package com.ll.kotudy.word.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MyWordTest {

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void init() {
        MyWord wordA = new MyWord("사진", "명사",
                "물체를 있는 모양 그대로 그려 냄. 또는 그렇게 그려 낸 형상.");
        MyWord wordB = new MyWord("동물", "명사",
                "사람을 제외한 길짐승, 날짐승, 물짐승 따위를 통틀어 이르는 말.");
        em.persist(wordA);
        em.persist(wordB);

        em.flush();
        em.clear();
    }

    @Test
    void entityDefaultCountValueTest() {
        List<MyWord> words = em.createQuery("select w from MyWord w", MyWord.class).getResultList();

        for (MyWord word : words) {
            assertThat(word.getCount()).isEqualTo(0);
        }
    }

    @Test
    void entityCountPlusTest() {
        List<MyWord> words = em.createQuery("select w from MyWord w", MyWord.class).getResultList();

        words.forEach(MyWord::plusCount);

        for (MyWord word : words) {
            assertThat(word.getCount()).isEqualTo(1);
        }
    }
}