# blog
블로그 검색 서비스

## jar 파일 다운로드 경로
- https://github.com/shfksekdrms2/blog_search/raw/master/sj_blog_search.jar

## 실행 방법 & 사용 방법
- java -jar sj_blog_search.jar
- localhost:8080/swagger-ui/index.html 접속
    - 블로그 검색 > 블로그 검색을 위한 메소드
        - /blog/search 를 통해 블로그 검색
    - 블로그 인기 검색어 목록 > 블로그 인기 검색어 검색을 위한 메소드
        - /blog/popular/keyword 를 통해 인기 검색어 목록 조회

- h2 console 접속
    - http://localhost:8080/h2-console 에 접속 하여 db 확인 가능
        - saved Settings: Generic H2 (Server)
        - Driver Class: org.h2.Driver
        - JDBC URL: jdbc:h2:mem:testdb
        - User Name: sa
        - Password: 

## 기능 구현
1. 블로그 검색
- get localhost:8080/blog/search?keyword={}&sortType={ACCURACY, RECENCY}&page={1,50}&size={1,50}
- 키워드 검색
    - queryParam 형태로 keyword 에 검색어 입력
- Pagination 
    - "pageInfo": 
        {
          "currentPage": 1,
          "size": 10,
          "totalPage": 0,
          "totalCount": 0
        }
- sortType 옵셔널
  - ACCURACY 기본
  - ACCURACY(정확도), RECENCY(최신순) 형태로 제공
- keyword 필수값
- page
  - 1 ~ 50 정수
- size
  - 1 ~ 50 정수

2. 인기 검색어
- get localhost:8080/blog/popular/keyword
- 많이 검색한 순서대로, 최대 10개의 검색 키워드 제공
- Redis sortedSet 를 활용하여 캐싱 처리
- 검색어 별로 검색된 횟수 표시
    - "popularKeywordTopTenList":
        {
          "keyword": "많이 검색된 키워드",
          "count": 검색된 횟수
        }

## 추가 기능
- 스웨거
- 하이버네이트 벨리데이션 사용
- webclient 로 api 연동
- naver blog 조회 연동
- 동시성 이슈 
  - log 로 남겨서 합계를 가져오도록 구현
- 멀티 모듈 구성
- 전역 에러 처리
- 테스트 케이스 추가
- 인기 검색어 목록은 redis 에서 조회
  - sorted set 을 이용하여 랭킹 조회
- 전략 패턴을 사용
  - 다음 -> 네이버 순으로 조회 하도록 구현 
    - linkedMap 을 이용하여 ClientType 에 있는 데이터를 순차적으로 넣어서 호출 순서 관리
  - 둘다 장애가 발생하였을 경우에 빈 데이터 내려준다.
- 외부 API 사용시 ehCache 를 적용
  - 60초 적용
- 키워드 검색시 검색어 카운팅 하는 로직 비동기 처리
  - 조회랑 상관없는 동작이기 때문에 비동기 처리

## 제약 사항
- 외부 API(다음, 네이버) 경우 변경이 1분 동안 inMemory 를 이용하여 캐시 해둠.
  - 캐시가 지워지면 다시 성능이 안좋아지는 단점이 있다.
  - 고도화 한다면, 주기적으로 외부 API 를 캐싱하여 사용하게 설계 예정
    - 예를 들어, 많이 검색되는 탑 키워드와 조건들(page 1, size 10)을 스케줄러로 캐싱 처리
- redis 기본(6379) 포트 사용
- 웹서버 8080 포트 사용
- 외부 API 조회시에 redis 대신 inMemory(ehcache) 를 사용한 이유
  - 로컬 캐싱을 통해 외부 API 요청을 하지 않도록 성능 고도화
  - 하지만, 다중 인스턴스를 사용하는 경우에는 redis 를 이용하여 인스턴스 통합 캐싱을 사용한다.