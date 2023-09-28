package com.ll.kotudy.word.controller;

import com.ll.kotudy.member.dto.reqeust.TokenHeaderRequest;
import com.ll.kotudy.word.dto.request.MyWordAddRequest;
import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import com.ll.kotudy.word.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/word")
    public EntityModel<SearchedWordsResponse> search(
            @Valid @NotEmpty(message = "요청 단어는 공백이 불가합니다.") @RequestParam String q)
            throws XPathExpressionException, IOException, ParserConfigurationException,
            SAXException, NoSuchAlgorithmException, KeyManagementException {

        SearchedWordsResponse response = dictionaryService.searchWords(q);

        return toModelSearchedWordsResponse(q, response);
    }

    private EntityModel<SearchedWordsResponse> toModelSearchedWordsResponse(String q, SearchedWordsResponse response)
            throws XPathExpressionException, IOException, ParserConfigurationException,
            SAXException, NoSuchAlgorithmException, KeyManagementException {

        return EntityModel.of(response,
                linkTo(methodOn(DictionaryController.class)
                        .search(q))
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(MyWordController.class)
                        .addMyWord(new TokenHeaderRequest("Authorization Token"), new MyWordAddRequest("word", "morpheme", "mean")))
                        .withRel("add-myWord")
                        .withType("POST")
        );
    }
}
