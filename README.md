# Spring Boot 노트 관리 API 서비스

이 프로젝트는 Spring Boot를 활용한 RESTful API 서비스로, Clean Architecture 원칙을 적용하여 개발되었습니다. 사용자 인증과 노트 관리 기능을 제공하며, 계층 분리를 통한 유지보수성과 테스트 용이성을 고려한 설계가 특징입니다.

## 배포 URL 
https://springboot-api-clean.onrender.com/
(무료 호스팅에 배포하여 접속에 몇 분 걸릴 수 있습니다.)

### 배포 환경
- Render 클라우드 호스팅에 Docker를 활용한 서비스 배포
- HTTPS 보안 프로토콜


## 주요 기능
- 사용자 관리
- 회원가입/로그인
- 사용자 프로필 조회 및 수정
- JWT 기반 인증
- 노트 관리
- 노트 작성, 조회, 수정, 삭제
- 태그 기능을 통한 노트 분류
- 페이지네이션 지원


## 기술 스택

### 백엔드
- Spring Boot 2.7
- Spring Security
- Spring Data JPA
- JWT
- H2 Database

### 프론트엔드

#### Thymeleaf 템플릿 엔진
- 서버사이드 렌더링을 위한 Thymeleaf 템플릿 엔진 활용
- 레이아웃 기반의 페이지 구성으로 재사용성 향상
- 동적 데이터 바인딩을 통한 사용자 인터페이스 구현
- Spring Security와 연동된 인증/인가 처리

### 개발 도구
- Gradle
- Swagger

## API 문서
Swagger UI를 통해 API 문서를 제공합니다.

## 프로젝트 특징
- 클린 아키텍처: 도메인 중심의 계층 분리로 유지보수성 향상
- 보안성: Spring Security와 JWT를 활용한 인증 구현
- 확장성: 모듈화된 구조로 새로운 기능 추가 용이
