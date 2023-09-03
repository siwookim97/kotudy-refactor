package com.ll.kotudy.word.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyWordRepository extends JpaRepository<MyWord, Long>, MyWordRepositoryCustom {

    boolean existsByNameAndMorpheme(String name, String morpheme);

    void deleteByNameAndMorpheme(String name, String morpheme);

    Optional<MyWord> findByNameAndMorphemeAndMean(String name, String morpheme, String mean);
}