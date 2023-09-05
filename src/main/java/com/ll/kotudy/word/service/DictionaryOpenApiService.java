package com.ll.kotudy.word.service;

import com.ll.kotudy.word.cache.OpenApiCache;
import com.ll.kotudy.word.domain.OpenApiCacheRepository;
import com.ll.kotudy.word.dto.SearchedWordDto;
import com.ll.kotudy.word.dto.WordSenceDto;
import com.ll.kotudy.word.dto.response.SearchedWordsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryOpenApiService {

    private final OpenApiCacheRepository openApiCacheRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${korean.dictionary.key}")
    private String key;

    @Value("${korean.dictionary.url}")
    private String urlString;

    @Value("${korean.dictionary.no-result}")
    private String noResult;

    public SearchedWordsResponse searchWords(String q)
            throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, NoSuchAlgorithmException, KeyManagementException {

        if (isWordCached(q)) {
            return getSearchedWordResponseFromCache(q);
        }

        String replacedQ = replaceBlank(q);
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(urlString);
        requestUrl.append("key=");
        requestUrl.append(key);
        requestUrl.append("&q=");
        // TODO: replacedQ 없으면 예외 처리 RequestDTO 에서 처리 NOT EMPTY로

        sslTrustAllCerts();
        String resultWord = trimXmlString(requestUrl.toString(), replacedQ);

        if (resultWord.contains(noResult)) {
            String msg = "표준 한국어 대사전 Open API를 통해 단어 " + replacedQ + "의 검색 결과가 없습니다.";
            List<SearchedWordDto> result = new ArrayList<>();

            return new SearchedWordsResponse(msg, result);
        }

        NodeList nodeList = getNodeList(resultWord);

        return parseXmlToResponse(replacedQ, nodeList);
    }

    private boolean isWordCached(String q) {
        return openApiCacheRepository.findById(q).isPresent();
    }

    private SearchedWordsResponse getSearchedWordResponseFromCache(String q) {
        List<SearchedWordDto> datum = openApiCacheRepository.findById(q).get().getDatum();

        return new SearchedWordsResponse("표준 한국어 대사전 Open API를 통해 단어" + q + "의 검색결과는 다음과 같습니다.", datum);
    }

    private String replaceBlank(String q) {
        return q.replaceAll(" ", "");
    }

    private SearchedWordsResponse parseXmlToResponse(String replacedQ, NodeList nodeList) {
        SearchedWordsResponse searchedWordsResponse = new SearchedWordsResponse();

        List<SearchedWordDto> searchedWordDtoList = new ArrayList<>();
        NodeList childWord = nodeList.item(0).getChildNodes();
        SearchedWordDto searchedWordDto = new SearchedWordDto();
        List<WordSenceDto> wordSenseDtoList = new ArrayList<>();

        for (int j = 0; j < childWord.getLength(); j++) {
            Node nodeWord = childWord.item(j);
            if ("target_code".equals(nodeWord.getNodeName())) {
                String target_codeString = nodeWord.getTextContent();
                int targetCode = Integer.parseInt(target_codeString);
                searchedWordDto.setTargetCode(targetCode);

            } else if ("word".equals(nodeWord.getNodeName())) {
                searchedWordDto.setWord(nodeWord.getTextContent());

            } else if ("pronunciation".equals(nodeWord.getNodeName())) {
                searchedWordDto.setPronunciation(nodeWord.getTextContent());

            } else if ("pos".equals(nodeWord.getNodeName())) {
                searchedWordDto.setPos(nodeWord.getTextContent());

            } else if ("sense".equals(nodeWord.getNodeName())) {
                WordSenceDto wordSenseDto = new WordSenceDto();
                StringBuilder definition = new StringBuilder();

                for (int h = 1; h < nodeWord.getTextContent().length(); h++) {
                    definition.append(nodeWord.getTextContent().charAt(h));
                }

                wordSenseDto.setSenseOrder(nodeWord.getTextContent().charAt(0) - 48);
                wordSenseDto.setDefinition(definition.toString());
                wordSenseDtoList.add(wordSenseDto);
                searchedWordDto.setWordSenceList(wordSenseDtoList);
            }
            searchedWordDtoList.add(searchedWordDto);
        }

        if (searchedWordDtoList.size() == 0) {

        }

        searchedWordsResponse.setMsg("표준 한국어 대사전 Open API를 통해 단어 " + replacedQ + "의 검색결과는 다음과 같습니다.");
        searchedWordsResponse.setData(searchedWordDtoList);
        openApiCacheRepository.save(new OpenApiCache(replacedQ, searchedWordDtoList));
        return searchedWordsResponse;
    }

    private void sslTrustAllCerts() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc;
        sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private String trimXmlString(String urlString, String replacedQ) throws IOException {
        StringBuilder sb = new StringBuilder();
        String finalQ = URLEncoder.encode(replacedQ, StandardCharsets.UTF_8);

        URL url = new URL(urlString + finalQ);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String lineWord;
        while ((lineWord = reader.readLine()) != null) {
            sb.append(lineWord.trim());
        }

        return sb.toString();
    }

    private NodeList getNodeList(String resultWord)
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        InputSource isWord = new InputSource(new StringReader(resultWord));
        DocumentBuilder builderWord = documentBuilderFactory.newDocumentBuilder();
        Document docWord = builderWord.parse(isWord);
        XPathFactory xpathFactoryWord = XPathFactory.newInstance();
        XPath xpathWord = xpathFactoryWord.newXPath();
        XPathExpression exprWord = xpathWord.compile("/channel/item");
        NodeList nodeList = (NodeList) exprWord.evaluate(docWord, XPathConstants.NODESET);

        return nodeList;
    }
}
