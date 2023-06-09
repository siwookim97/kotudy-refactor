package com.ll.kotudy.word.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyWordRepository extends JpaRepository<MyWord, Long>, MyWordRepositoryCustom {

    boolean existsByNameAndMorpheme(String name, String morpheme);

    void deleteByNameAndMorpheme(String name, String morpheme);
}