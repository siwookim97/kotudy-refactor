package com.ll.kotudy.word.service;

import com.ll.kotudy.word.domain.MyWordRepository;
import com.ll.kotudy.word.domain.TodayWord;
import com.ll.kotudy.word.domain.TodayWordRepository;
import com.ll.kotudy.word.dto.TodayWordDto;
import com.ll.kotudy.word.dto.response.TodayWordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayWordService {

    private static final String TODAY_WORD_RESULT = "오늘의 단어 목록입니다.";

    private final MyWordRepository myWordRepository;
    private final TodayWordRepository todayWordRepository;

//    @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void setupTodayWordList() {
        todayWordRepository.deleteAll();
        List<TodayWord> todayWordList = createTodayWordList();
        todayWordRepository.saveAll(todayWordList);

        log.info("=== 오늘의 단어가 갱신되었습니다 ===");
    }

    private List<TodayWord> createTodayWordList() {
        List<TodayWordDto> todayWordDtoList = myWordRepository.findRandomDistinctMyWords();
        List<TodayWord> todayWordList = todayWordDtoList.stream()
                .map(TodayWord::new)
                .collect(Collectors.toList());

        return todayWordList;
    }

    public TodayWordResponse getTodayWordList() {
        List<TodayWordDto> todayWordDtoList = getTodayWordDtoList();

        return new TodayWordResponse(TODAY_WORD_RESULT, todayWordDtoList);
    }

    private List<TodayWordDto> getTodayWordDtoList() {
        List<TodayWord> todayWordList = todayWordRepository.findAll();
        List<TodayWordDto> todayWordDtoList = todayWordList.stream()
                .map(TodayWordDto::new).collect(Collectors.toList());
        return todayWordDtoList;
    }
}