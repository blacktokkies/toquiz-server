# 🐰 toquiz
<p align="center"><img src="https://user-images.githubusercontent.com/72093196/235161403-da40733a-2f9f-4acf-932e-28cab2d316da.png" width=300 alt="toquiz"></p>

## 🗳️ 프로젝트 소개
toquiz(토퀴즈)는 사용자가 패널에 익명으로 질문을 올리고 답변을 받을 수 있는 서비스입니다.

세미나 혹은 OT의 마지막에는 질문을 할 수 있는 QnA 시간이 있습니다. 하지만, 종종 시간 부족으로 모든 질문에 답변하지 못하는 경우가 있었습니다. 이러한 불편함을 해결하기 위해 언제든지 실시간으로 QnA를 할 수 있는 toquiz 서비스를 개발하였습니다.

## 🛠️ 기술 스택
<img width="619" alt="image" src="https://github.com/blacktokkies/toquiz-server/assets/72093196/e2a9ac38-ab72-48ea-b5f0-64cb1a61f69d">

## 🕋 시스템 아키텍처
<img width="538" alt="image" src="https://github.com/blacktokkies/toquiz-server/assets/72093196/1a761df5-e6ca-4b47-99e3-d984c9c2ab89">


## ⚙️ 기능
> **회원 관리 기능**

<img src="https://github.com/blacktokkies/toquiz-server/assets/72093196/e1d59538-7da9-43af-9c1a-cda250e652f9" width="500">

- 회원가입, 로그인, 회원 정보 수정 및 탈퇴 기능이 있습니다. 
- JWT 기반의 토큰을 사용하여 로그인 상태 유지를 하고 있습니다.

> **익명으로 활동 가능한 패널 (패널은 질문과 답변을 올릴 수 있는 공간이다)**

<img src="https://github.com/blacktokkies/toquiz-server/assets/72093196/5fc316d2-4f12-451e-a01d-9841840e29c7" width="500">

- 로그인 하지 않은 사용자도 질문을 올릴 수 있습니다.
- 익명 사용자가 생성하거나 좋아요를 누른 질문의 정보는 패널에 다시 접속해도 유지됩니다.

> **실시간 질문, 답변 생성 및 좋아요 활성화를 통한 Live Communication**

<img src="https://github.com/blacktokkies/toquiz-server/assets/72093196/1e7514d1-7d65-4557-bfcf-23e8769f5a05" width="500">

- 답변 생성은 오직 패널 생성자만 가능합니다.
- 질문 또는 답변이 생성되거나, 질문에 좋아요가 눌리면, 실시간으로 확인할 수 있습니다.




## 🤔 고민한 사항
[🗂️ 데이터를 어떤 DB에 저장하는 것이 좋을까?](https://www.sooman.site/db--761c3f19c89a430bbb081adc44ae72e5)

[🫥 익명 사용자 활동 정보 저장](https://www.sooman.site/41c171801dc942d2b013e4f74ddb7da8)

[🪪️ 패널 식별자를 Auto Increment에서 고유 식별자로 변경하게 된 이유](https://www.sooman.site/auto-increment--41b0aee3a8cc4517bd9fe98dd40bc3dc)

## 📌 ETC
[Team Notion](https://black-tokkies.notion.site/toquiz-0ba770856ed24ba39bdec1636d23b3ab?pvs=4)

[Commit Convetion](https://black-tokkies.notion.site/Github-commit-convention-Server-1c19eefe8be6467797c159cd8c30e394?pvs=4)