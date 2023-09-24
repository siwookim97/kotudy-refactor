package com.ll.kotudy.word.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyWordRepository extends JpaRepository<MyWord, Long>, MyWordRepositoryCustom {

    Optional<MyWord> findByNameAndMorphemeAndMean(String name, String morpheme, String mean);
}