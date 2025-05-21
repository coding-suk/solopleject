# solopleject
# 🛒 E-Commerce 프로젝트 API 명세서

> 본 프로젝트는 Java + Spring Boot 기반으로 개발된 커머스 백엔드 시스템입니다. 사용자, 상품, 주문, 결제 등 주요 기능을 API 형태로 제공합니다.

---

## 📚 기술 스택

- Language: Java 17
- Framework: Spring Boot 3.x
- DB: MySQL
- ORM: Spring Data JPA
- Security: Spring Security + JWT
- Test: JUnit5, Mockito
- 기타: Redis, Swagger, Postman

## 📌 프로젝트 개요

Spring Boot 기반으로 구현된 E-Commerce 백엔드 API 프로젝트입니다.  
회원가입부터 상품 관리, 주문/결제, 관리자 대시보드까지 모든 기능을 혼자 설계하고 구현하였습니다.

---

## 📌 목차

1. [사용자 기능 (회원)](#사용자-기능-회원)
2. [상품 기능](#상품-기능)
3. [장바구니 기능](#장바구니-기능)
4. [주문 기능](#주문-기능)
5. [결제 기능](#결제-기능)
6. [배송 기능](#배송-기능)
7. [리뷰 기능 (선택)](#리뷰-기능-선택)
8. [관리자 기능](#관리자-기능)

---

## ✅ 사용자 기능 (회원)

### 회원가입
- **[POST]** `/api/users/signup`
- **설명**: 회원가입 처리
- **Request Body**
```json
{
  "email": "user@example.com",
  "password": "1234pass",
  "name": "홍길동",
  "role": "USER"
}
```
- **Response**
```json
{
  "userId": 1,
  "message": "회원가입 성공"
}
```

### 로그인 (JWT 발급)
- **[POST]** `/api/users/login`
- **설명**: 로그인 처리 및 토큰 발급
- **Request Body**
```json
{
  "email": "user@example.com",
  "password": "1234pass"
}
```
- **Response**
```json
{
  "accessToken": "JWT_ACCESS",
  "refreshToken": "JWT_REFRESH"
}
```

### 회원탈퇴
- **[DELETE]** `/api/users`

### 회원정보 수정
- **[PUT]** `/api/users/info`

### 비밀번호 재설정
- **[POST]** `/api/users/reset-password`

### 권한 구분
- USER / SELLER / ADMIN (JWT Claim 기반 권한 분기)

---

## 🛍️ 상품 기능

### 상품 등록
- **[POST]** `/api/products`

### 상품 수정
- **[PUT]** `/api/products/{productId}`

### 상품 삭제
- **[DELETE]** `/api/products/{productId}`


권한 분리는 Spring Security + Role 기반 인가 처리

일부 예시 응답은 생략했으며, Swagger 또는 Postman 문서로도 제공 예정
