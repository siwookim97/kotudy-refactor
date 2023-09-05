package com.ll.kotudy.word.cache;

import com.ll.kotudy.word.dto.SearchedWordDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.List;

@Getter
@NoArgsConstructor
@RedisHash(value = "openApi", timeToLive = 600L)
public class OpenApiCache {

    @Id
    private String id;

    private List<SearchedWordDto> datum;

    public OpenApiCache(String word, List<SearchedWordDto> datum) {
        this.id = word;
        this.datum = datum;
    }
}
