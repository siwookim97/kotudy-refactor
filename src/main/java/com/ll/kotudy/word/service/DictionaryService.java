package com.ll.kotudy.word.service;

import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface DictionaryService {

    SearchedWordsResponse searchWords(String target)
            throws IOException, SAXException, ParserConfigurationException,
            XPathExpressionException, NoSuchAlgorithmException, KeyManagementException;
}
