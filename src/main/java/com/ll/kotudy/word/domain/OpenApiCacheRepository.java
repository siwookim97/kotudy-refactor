package com.ll.kotudy.word.domain;

import com.ll.kotudy.word.cache.OpenApiCache;
import org.springframework.data.repository.CrudRepository;

public interface OpenApiCacheRepository extends CrudRepository<OpenApiCache, String> {
}
