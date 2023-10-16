<div align="center">
  
# kotudy 팀 프로젝트, 나혼자 리팩토링
<img src="https://github.com/siwookim97/kotudy-refactor/assets/72070679/a33ab99b-0109-4bae-bd53-40fc2f857e37" width="75%" height="75%">

</div>

> 팀 프로젝트로 진행했던 프로젝트의 부족한 점을 보완하고 학습하기 위해 **리팩토링 프로젝트**를 시작하게 되었습니다.
>
> 본 프로젝트는 **아동을 위한 한국어 학습 웹 프로젝트** 라는 주제로 진행되었습니다.
>
> 본 서비스를 통해 학습하고자 하는 학생들에게 손쉬운 한국어 단어 학습을 제공하고 현재 문제가 되는 청소년들의 떨어지는 문해력의 문제를 해결하고자 합니다.
>
> 이미지 하단의 아이콘을 통해 프로젝트 진행 **블로그**와 **리팩토링 이전의 Github Repository**를 확인하실 수 있습니다.
>


<div align="center">

[<img src="https://img.shields.io/badge/-블로그-important?style=for-the-badge&logo=google-chrome&logoColor=white" />](https://gosiwoo.tistory.com/category/Develop/%ED%8C%80%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%2C%20%EB%82%98%ED%99%80%EB%A1%9C%20%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81)
[<img src="https://img.shields.io/badge/-이전%20프로젝트%20Github-gray?style=for-the-badge&logo=github&logoColor=white" />](https://github.com/slowStarter-OIDC/Literacy-Improvement-Web)

</div>

<br>

## 🚀 주요 기능


#### 1. 회원 기능

> JWT 토큰 기반의 회원 기능을 이용할 수 있습니다.

#### 2. 단어사전 검색 기능

> 한국어 대사전 Open API를 통해 단어를 검색해 나만의 단어장에 넣을 수 있습니다.

#### 3. 나만의 단어장 기능

> 나만의 단어장으로 한국어 학습을 할 수 있습니다.

#### 4. 퀴즈 기능

> 나만의 단어장을 기반으로 생성된 퀴즈를 통해 한국어 학습을 더욱 쉽게 진행할 수 있습니다.

#### 5. 랭킹 기능

> 퀴즈 기능을 통해 획득한 점수로 학습의 동기를 더욱 이끌어낼 수 있습니다.

#### 6. 오늘의 단어 기능

> 매일 자정마다 바뀌는 오늘의 단어로 다른 회원들이 어려워 하는 단어들을 학습할 수 있습니다.

<br>

## 📄 API 문서
각 기능들의 호출 방법은 다음 API 문서를 통해 확인할 수 있습니다.

**[API 명세](https://siwookim97.github.io/kotudy-refactor/src/main/resources/static/docs/index.html)**

<br>

## 📁 프로젝트 구조
```bash
├─java
│  └─com
│      └─ll
│          └─kotudy
│              │  KotudyRefactorApplication.java
│              │  
│              ├─config
│              │  │  CacheConfig.java
│              │  │  EncoderConfig.java
│              │  │  JpaAuditingConfig.java
│              │  │  QuerydslConfig.java
│              │  │  WebConfig.java
│              │  │  
│              │  └─auth
│              │          AuthenticationConfig.java
│              │          JwtFilter.java
│              │          JwtProvider.java
│              │          
│              ├─logging
│              │      CacheEventLogger.java
│              │      LoggingAspect.java
│              │      
│              ├─member
│              │  ├─controller
│              │  │      MemberController.java
│              │  │      
│              │  ├─domain
│              │  │      Member.java
│              │  │      MemberRepository.java
│              │  │      MemberRole.java
│              │  │      
│              │  ├─dto
│              │  │  ├─reqeust
│              │  │  │      MemberJoinRequest.java
│              │  │  │      MemberLoginRequest.java
│              │  │  │      TokenHeaderRequest.java
│              │  │  │      
│              │  │  └─response
│              │  │          JoinResponse.java
│              │  │          LoginResponse.java
│              │  │          SearchMemberResponse.java
│              │  │          
│              │  └─service
│              │          MemberService.java
│              │          
│              ├─util
│              │  ├─baseEntity
│              │  │      BaseEntity.java
│              │  │      
│              │  └─exception
│              │          AppException.java
│              │          ErrorCode.java
│              │          ErrorResponse.java
│              │          GlobalExceptionHandler.java
│              │          
│              └─word
│                  ├─controller
│                  │      DictionaryController.java
│                  │      MyWordController.java
│                  │      QuizController.java
│                  │      TodayWordController.java
│                  │      
│                  ├─domain
│                  │      MemberMyWord.java
│                  │      MemberMyWordRepository.java
│                  │      MyWord.java
│                  │      MyWordRepository.java
│                  │      MyWordRepositoryCustom.java
│                  │      MyWordRepositoryImpl.java
│                  │      TodayWord.java
│                  │      TodayWordRepository.java
│                  │      
│                  ├─dto
│                  │  │  MemberRankingDto.java
│                  │  │  QuizForm.java
│                  │  │  QuizWordDto.java
│                  │  │  SearchedWordDto.java
│                  │  │  TodayWordDto.java
│                  │  │  WordSenceDto.java
│                  │  │  
│                  │  ├─request
│                  │  │      MyWordAddRequest.java
│                  │  │      MyWordSearchRequest.java
│                  │  │      QuizResultRequest.java
│                  │  │      
│                  │  └─response
│                  │          MyWordAddResponse.java
│                  │          MyWordDeleteResponse.java
│                  │          MyWordResponse.java
│                  │          MyWordSearchResponse.java
│                  │          QuizResponse.java
│                  │          QuizResultResponse.java
│                  │          RankingNonMemberResponse.java
│                  │          RankingResponse.java
│                  │          SearchedWordsResponse.java
│                  │          TodayWordResponse.java
│                  │          
│                  └─service
│                          DictionaryService.java
│                          DictionaryServiceImpl.java
│                          MyWordService.java
│                          QuizService.java
│                          TodayWordService.java
│                          
└─resources
    │  application-redis.yml
    │  application-secret.yml
    │  application.yml
    │  ehcache.xml
    │  
    ├─META-INF
    ├─static
    │  └─docs
    │          index.html
    │          
    └─templates
```

<br>

## 🛠 기술 스택

![image](https://github.com/siwookim97/kotudy-refactor/assets/72070679/62a65918-9ec3-46ab-a8e5-f3205ccc80aa)
