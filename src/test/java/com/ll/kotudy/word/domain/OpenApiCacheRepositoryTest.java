package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.cache.OpenApiCache;
import com.ll.kotudy.word.dto.SearchedWordDto;
import com.ll.kotudy.word.dto.WordSenceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class OpenApiCacheRepositoryTest {

    @Autowired
    private OpenApiCacheRepository openApiCacheRepository;

    @Test
    void repositoryTest() {
        List<SearchedWordDto> data = new ArrayList<>();
        List<WordSenceDto> wordSenceDtos = new ArrayList<>();
        WordSenceDto senceDto = new WordSenceDto(1, "의미가 없잖아!");
        wordSenceDtos.add(senceDto);
        SearchedWordDto dto = new SearchedWordDto(1, "test", "pronunciation", "pos1", wordSenceDtos);
        data.add(dto);
        openApiCacheRepository.save(new OpenApiCache("test", data));

        OpenApiCache openApiCache = openApiCacheRepository.findById("test").get();
        System.out.println("openApiCache.getWord() = " + openApiCache.getId());
        System.out.println("openApiCache.getDatum() = " + openApiCache.getDatum());
    }
}