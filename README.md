# 🌧️ 빗속말 (Raindrop) - Backend

**빗속말은 비오는 날에만 익명 메시지를 확인할 수 있는 모바일 웹 서비스의 백엔드 서버입니다. 사용자 인증, 익명 메시지 관리, 게시판 생성, 위치 데이터 저장 등을 처리합니다.**

## 배포 링크
### [RainDrop](https://raindrop-front.vercel.app/)

<p align="center">🏡 LG U+ 유레카 2기 백엔드반 6조 미니 프로젝트</p>
<p align="center"><img src=https://avatars.githubusercontent.com/u/202894428?s=200&v=4 /></p>

## 💧 서비스 개요

빗속말 백엔드는 사용자 인증, 메시지 처리, 날씨 데이터 연동을 담당하는 서버입니다. 프론트엔드와 REST API로 통신하며, 다음과 같은 주요 기능을 제공합니다:

- **🔐 사용자 인증**: 카카오 OAuth를 통한 회원 관리
- **💬 메시지 관리**: 익명 메시지의 저장, 조회, 삭제 처리
- **🌍 위치 및 날씨 연동**: 사용자 위치 기반 날씨 정보 제공
- **🔒 데이터 암호화**: 사용자 정보 및 메시지 데이터 암호화

## ✨ 주요 기능

| 기능 | 설명 |
|------|------|
| **카카오 OAuth 인증** | 카카오 로그인 API를 통한 사용자 인증 및 토큰 관리 |
| **사용자 관리** | 사용자 정보 저장 및 조회, 첫 로그인 시 자동 회원가입 |
| **메시지 CRUD** | 익명 메시지 생성, 조회, 수정, 삭제 API |
| **Scene 관리** | 사용자별 고유 메시지 공간(Scene) 생성 및 관리 |
| **날씨 데이터 연동** | 외부 날씨 API와 연동하여 현재 위치의 날씨 상태 확인 |
| **데이터 암호화** | 민감 정보 암호화 및 보안 처리 |

## 🛠️ 기술 스택

<p align="center">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</p>

### 백엔드
- **언어 & 프레임워크**: Java 17, Spring Boot 3.2
- **데이터베이스**: MySQL 8.0
- **ORM**: MyBatis
- **인증**: JWT, Kakao OAuth
- **API 문서화**: Swagger, SpringDoc
- **암호화**: Jasypt
- **빌드 도구**: Gradle

## 📝 API 명세

### 인증 관련

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/user/login` | 카카오 인증코드로 로그인 |
| GET | `/api/user/info` | 현재 사용자 정보 조회 |
| POST | `/api/user/logout` | 로그아웃 처리 |

### 메시지 관련

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/messages` | 메시지 목록 조회 |
| GET | `/api/messages/{id}` | 특정 메시지 상세 조회 |
| POST | `/api/messages` | 새 메시지 작성 |
| DELETE | `/api/messages/{id}` | 메시지 삭제 |

### Scene 관련

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/scenes` | 사용자의 Scene ID 조회 |
| POST | `/api/scenes` | 새 Scene 생성 |
| GET | `/api/scenes/{encryptedId}` | 특정 Scene 조회 |

### 날씨 관련

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/weather` | 현재 위치의 날씨 정보 조회 |
| POST | `/api/weather/location` | 사용자 위치 정보 업데이트 |

## 👥 팀원 소개

<table align="center">
  <tr>
    <td align="center"><b>한동찬</b></td>
    <td align="center"><b>이시현</b></td>
    <td align="center"><b>박지회</b></td>
    <td align="center"><b>이은비</b></td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/100357408?v=4" width="120" height="120"/>
    </td>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/102678331?v=4" width="120" height="120"/>
    </td>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/197379577?v=4" width="120" height="120"/>
    </td>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/108103346?v=4" width="120" height="120"/>
    </td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/pillow12360">@pillow12360</a></td>
    <td align="center"><a href="https://github.com/sihyuuun">@sihyuuun</a></td>
    <td align="center"><a href="https://github.com/jihoi0615">@jihoi0615</a></td>
    <td align="center"><a href="https://github.com/silverain02">@silverain02</a></td>
  </tr>
</table>

## 📦 주요 구현 내용

### 사용자 인증 및 관리
- Kakao OAuth 2.0 기반 소셜 로그인 구현
- JWT 토큰 기반 인증 및 인가 처리
- 첫 로그인 시 자동 회원가입 로직

### 메시지 및 Scene 관리
- 사용자별 고유 Scene 생성 및 ID 암호화
- 익명 메시지 저장 및 조회 기능
- 사용자 권한 기반 메시지 관리 (삭제 등)

### 날씨 연동 시스템
- 외부 날씨 API 연동 및 데이터 가공
- 위치 기반 실시간 날씨 정보 제공
- 날씨 상태에 따른 메시지 접근 제어

### 보안 및 데이터 처리
- 민감 정보 암호화 (Jasypt 활용)
- DB 정규화 및 효율적인 쿼리 최적화
- 예외 처리 및 로깅 시스템 구축

## 📄 라이센스
MIT License
