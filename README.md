# 이야기꾼(동화책 제작툴)

- 개발 진행 일정: 2023/04/09 ~ 2023/07/14


<img src="./assets/Untitled.png" width=80%>
<br>

## 🙇‍♀️ 팀원소개

`Create & Use`  저희 팀을 소개합니다! 

<table>
  <h3>사이트 개발</h3>
  <tbody>
    <tr>
      <td align="center"><a href="https://avatars.githubusercontent.com/u/64175384?v=4"><img src="https://avatars.githubusercontent.com/u/64175384?v=4" width="150px;" alt=""/><br /><sub><b>최연주</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/G-Lake"><img src="https://avatars.githubusercontent.com/u/86152185?v=4" width="150px;" alt=""/><br /><sub><b>강병관</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/SoonDhi"><img src="https://avatars.githubusercontent.com/u/103040908?v=4" width="150px;" alt=""/><br /><sub><b> 박순범</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>
  <h3>툴 개발</h3>
  <table>
  <tbody>
      <td align="center"><a href="https://github.com/kimwonny8"><img src="https://avatars.githubusercontent.com/u/86811808?v=4" width="150px;" alt=""/><br /><sub><b>김정원</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/Yehi-or"><img src="https://avatars.githubusercontent.com/u/103096598?v=4" width="150px;" alt=""/><br /><sub><b>변성환</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/GeonHo-Ko"><img src="https://avatars.githubusercontent.com/u/77083515?v=4" width="150px;" alt=""/><br /><sub><b>고건호</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>


<br>

## 📄 사용 기술 스택

```
HTML, CSS, JavaScript, Vue.js 2, Axios

Java 17, Spring Boot 2.7.8, Spring Security, Spring Data JPA, JWT

OpenAI API, Kakao API, Iamport API

MariaDB, MongoDB

GitHub action, AWS, Vercel

GitHub, Notion, Discord, IntelliJ, Visual Studio Code
```

<br>


## 📄 기획 소개

- 부모님이 동화책 제작 툴을 통해 자녀를 위한 동화책을 간편하게 만들 수 있는 서비스
- **chat gpt 를 이용한 시나리오 작성, 이야기 흐름 파악**
- **추가한 자막에 다양한 tts 또는 내 목소리를 녹음해 들을 수 있는 기능**
- 완성된 작품은 사이트에서 감상할 수 있습니다.


[이야기꾼 기획기능설명.pdf](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c803260f-ca75-407e-9ab0-06ded8feed77/%EC%9D%B4%EC%95%BC%EA%B8%B0%EA%BE%BC_%EA%B8%B0%ED%9A%8D%EA%B8%B0%EB%8A%A5%EC%84%A4%EB%AA%85.pdf)

<br>

## 📄 기능

- 제작 툴
  - 시나리오 선택 및 흐름 파악
    - 키워드 입력받아서 시나리오 작성
    - 시나리오 다시 받기
    - 흐름 파악
  - 감상 미리보기
    - 페이지 추가 / 이동 / 삭제
    - 미리 보기
  - 캐릭터 / 배경 / 자막
    - 캐릭터/배경/자막 추가
    - 파일 업로드
  - 녹음 / TTS
    - 본인의 목소리 직접 녹음 가능
    - 여자아이, 남자아이, 할머니, 할아버지 등 모든 목소리 지원
- 사이트
  - 회원가입 / 로그인
  - 작품 상세 페이지
    - 댓글, 추천, 결제
  - 작품감상
  - 마이페이지
    - 작품 결제 / 엽전 충전 / 환급
    - 회원 정보 수정 / 탈퇴
  - 관리자 페이지

<br>

## 📄 툴 스토리보드

<details>
<summary>보기</summary>
<div markdown="1">

<img src="./assets/Untitled 3.png" width=80%>

<img src="./assets/Untitled 4.png" width=80%>

<img src="./assets/Untitled 5.png" width=80%>

</div>
</details>


<br>

## 📄 DB 설계

<img src="./assets/Untitled 6.png" width=70%>


<br>


## 🖥️ CI/CD 구축

<img src="./assets/image-20230627154438165.png" width=60%>

- GitHub에 푸시하고 메인에 머지되면 GitHub Action을 통해 yml 파일이 실행됩니다.
- 백엔드를 Build 하고, 압축파일을 AWS S3에 업로드 후 CodeDeploy를 이용해 EC2에 배포되게 구축하였습니다.

<br>

## 🖥️ 프론트 엔드 배포

<img src="./assets/Untitled 1.png" width=30%> <img src="./assets/Untitled 2.png" width=30%>

- 프론트엔드는 Vercel을 이용하여 깃허브와 연동시켜두었습니다.



<br>

## 🖥️ 백엔드 서비스 아키텍처

<img src="./assets/image-20230627154447542.png" width=60%>

- 도메인을 Route53으로 연결하고, HTTPS 로 전환하기위해 인증서를 받아 EC2에 연결하였습니다.
- HTTP로 들어온 주소는 HTTPS로 바꾸기 위해 Nginx 를 사용하였고, DB는 RDS에 MariaDB, mongoDB Atlas로 mongoDB를 연결하였습니다.

<br><br>


## 📖 실행화면
<details>
<summary>보기</summary>
<div markdown="1">

### 메인

- 메인페이지의 메뉴에는 인기순, 최신순, 만들어보기(툴로 이동) 가 있습니다.

<img src="./assets/Untitled 7.png" width=80%>

### 툴 진입 시 미완성 작품 리스트

- 만들어보기 진입 시,
  새로운 동화책을 만들 수 있고, 기존에 만들다 만 작품을 불러와 작업할 수 있습니다.

<img src="./assets/Untitled 8.png" width=80%>

### 툴 진입 후

- 시나리오 추천 받기를 클릭하면 OpenAI API를 통해 시나리오를 받을 수 있고,
  직접 쓰기를 클릭하면 본인이 직접 내용을 작성하면 됩니다.

<img src="./assets/Untitled 9.png" width=80%>

<img src="./assets/Untitled 10.png" width=80%>

### 툴 시나리오 제공 받기

- Open AI API 는 받아오는 데 시간이 조금 걸리기 때문에 로딩이 되고, 이 때 다른 작업을 다 할 수 있습니다.

<img src="./assets/Untitled 11.png" width=80%>

<img src="./assets/Untitled 12.png" width=80%>

### 흐름파악하기

<img src="./assets/Untitled 13.png" width=80%>

### 배경 / 캐릭터 / 자막 / 페이지 / TTS / 음성 녹음 / 감상

- 배경, 캐릭터, 페이지를 드래그 앤 드롭으로 추가할 수 있습니다.

<img src="./assets/Untitled 14.png" width=80%>

- 캐릭터와 배경은 크기 조절 및 순서 변경이 가능합니다.

<img src="./assets/Untitled 15.png" width=80%>

- 자막은 크기 조절 및 색상 변경도 가능하며, 다양한 목소리의 TTS도 추가할 수 있습니다.

<img src="./assets/Untitled 16.png" width=80%>

- 페이지도 순서 이동 및 삭제가 가능하고, TTS 대신 직접 녹음도 할 수 있습니다.

<img src="./assets/Untitled 17.png" width=80%>

- 만든 페이지들을 미리보기가 가능하고, 저장된 음성은 자동 재생됩니다.

<img src="./assets/Untitled 18.png" width=80%>

### 작품 제출

- 해당 작품을 제출하면 메인에 노출되어 많은 사람들이 감상할 수 있습니다.

<img src="./assets/Untitled 19.png" width=80%>

### 작품 상세 페이지

- 작가가 아닌 다른 사람은 추천, 즐겨찾기, 댓글 작성이 가능하며, 결제 후 감상도 가능합니다.

<img src="./assets/Untitled 20.png" width=80%>

<img src="./assets/Untitled 21.png" width=80%>

### 마이 페이지 - 충전은 카카오 페이, 카드 결제, 무통장 입금

- 엽전(코인)을 충전할 수 있고 내역을 볼 수 있습니다.
- 충전은 카카오 페이, 카드 결제, 무통장 입금이 가능합니다.

<img src="./assets/Untitled 22.png" width=80%>

### 작품 관리 및 회원 정보 관리

- 내가 만든 작품, 즐겨찾기한 작품, 결제한 작품 리스트를 볼 수 있습니다.

<img src="./assets/Untitled 23.png" width=80%>

<img src="./assets/Untitled 24.png" width=80%>


</div>
</details>

