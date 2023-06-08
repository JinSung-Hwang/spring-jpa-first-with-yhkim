김영한의 "실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발"을 수강하며 작성한 Code를 저장한 Repository입니다.

### Lombok 세팅

1. preference 열기
2. plugins 선택
3. market 탭 선택
4. lombok 설치
4. Apply 선택
5. OK 선택
1. 다시 preference 열기
2. annotation processors 검색
3. enable annotation processing 체크
4. Apply 선택
5. OK 선택

### 사용되는 라이브러리

1. spring-web
1. thymeleaf
1. lombok
1. h2
1. jpa
1. devtools (html화면에서 build-> recompile하면 서버 재시작안하고 파일만 recompile한다.)
```
implementation 'org.springframework.boot:spring-boot-devtools'
```
