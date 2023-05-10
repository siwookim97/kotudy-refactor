package com.ll.kotudy.word.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyWordRepository extends JpaRepository<MyWord, Long> {
}
