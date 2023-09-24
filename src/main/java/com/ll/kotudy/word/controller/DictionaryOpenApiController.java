package com.ll.kotudy.word.controller;

import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import com.ll.kotudy.word.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dictionary")
public class DictionaryOpenApiController {

    private final DictionaryService dictionaryService;

    @GetMapping("/word")
    public ResponseEntity<SearchedWordsResponse> search(
            @Valid @NotEmpty(message = "요청 단어는 공백이 불가합니다.") @RequestParam String q)
            throws XPathExpressionException, IOException, ParserConfigurationException,
            SAXException, NoSuchAlgorithmException, KeyManagementException {

        SearchedWordsResponse response = dictionaryService.searchWords(q);

        return ResponseEntity.ok(response);
    }
}
