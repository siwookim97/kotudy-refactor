package com.ll.kotudy.word.controller;

import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import com.ll.kotudy.word.service.DictionaryOpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchWordController {

    private final DictionaryOpenApiService dictionaryOpenApiService;
    @GetMapping("/word")
    public ResponseEntity<SearchedWordsResponse> searchWords()
            throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, NoSuchAlgorithmException, KeyManagementException {
        SearchedWordsResponse response = dictionaryOpenApiService.searchWords("배");

        return ResponseEntity.ok(response);
    }
}
