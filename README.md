# solopleject
# 🛒 E-Commerce 프로젝트 API 명세서

> Java + Spring Boot + React 기반의 개인 커머스 프로젝트입니다. 회원가입부터 상품 관리, 장바구니, 주문/결제, 포인트 적립, 쿠폰 발급/소멸, 알림 전송까지 전체 기능을 혼자 설계 및 개발하였습니다.

---

## 📚 기술 스택

### 백엔드
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security + JWT
- **Caching & Session**: Redis (Cloud 기반)
- **Test**: JUnit5, SpringBootTest (통합 테스트 위주)
- **Infra**: AWS EC2, S3, Redis Cloud
- **Etc**: Swagger, Postman

### 프론트엔드
- **Language**: JavaScript (ES6+)
- **Framework**: React (Vite + React Router)
- **상태 관리**: Context API (AuthContext, CartContext)
- **라이브러리**: Axios, React Toastify
- **배포**: AWS S3 (정적 사이트)

---

## 🎯 프로젝트 주요 기능

- **회원가입 / 로그인 / JWT 인증**
- **상품 등록, 조회, 수정, 삭제 (판매자 전용)**
- **장바구니 기능 (비회원 포함 + Redis TTL 저장)**
- **로그인 시 장바구니 병합**
- **주문 생성 / 결제 처리**
- **포인트 적립 및 만료**
- **쿠폰 발급/조회/사용/소멸 스케줄링**
- **회원 생일/가입/첫 구매 시 쿠폰 자동 발급**
- **프론트 연동 완료 (React + JWT 기반 인증 유지)**
- **마이페이지: 주문 내역 / 보유 쿠폰 조회**
- **관리자 대시보드 기획 중**

---

## 📁 목차

1. [API 명세](#api-명세-요약)
2. [도메인 모델 요약](#도메인-모델-요약)
3. [구현 기능 상세](#구현-기능-상세)
4. [구현 포인트](#구현-포인트)
5. [실행 방법](#실행-방법)

---

## ✅ API 명세 요약

> (대표 API 일부 예시, 전체 명세는 Swagger 또는 Postman 문서 제공)

### 🔐 인증
- `POST /ecomos/auth/signup` 회원가입
- `POST /ecomos/auth/signin` 로그인 (JWT 발급)
- `GET /ecomos/auth/me` 로그인 상태 확인

### 👤 사용자
- `GET /ecomos/users/me` 내 정보 조회
- `PUT /ecomos/users/info` 내 정보 수정
- `DELETE /ecomos/users` 회원 탈퇴

### 🛍️ 상품
- `GET /ecomos/sellers/products` 전체 상품 조회
- `POST /ecomos/sellers/products` 상품 등록 (판매자)
- `PUT /ecomos/sellers/products/{id}` 상품 수정
- `DELETE /ecomos/sellers/products/{id}` 상품 삭제

### 🛒 장바구니
- Redis 기반 저장 (`guestId` or `userId` 키)
- `POST /ecomos/cart` 상품 추가
- `DELETE /ecomos/cart/{productId}` 상품 삭제
- `POST /ecomos/cart/merge` 로그인 시 병합 처리

### 📦 주문/결제
- `POST /ecomos/orders` 주문 생성
- `GET /ecomos/orders/my` 내 주문 내역 조회
- `POST /ecomos/payment/mockpay` 모의 결제 처리

### 💰 포인트
- 결제 시 적립, 일정 기간 후 자동 만료
- `GET /ecomos/points/history` 포인트 적립/차감 내역 조회

### 🎟️ 쿠폰
- `GET /ecomos/coupons/my` 내 보유 쿠폰 조회
- 자동 발급 정책 (가입/생일/첫구매 쿠폰)
- 만료 스케줄러 + 1일 전 알림

---

## 📦 도메인 모델 요약

- **User**: 회원 (Role: USER, SELLER, ADMIN)
- **Product**: 상품 정보
- **Cart (Redis)**: 비회원/회원 장바구니 저장소
- **Order / Payment**: 주문 및 결제 처리
- **Point / PointHistory**: 적립금 내역
- **Coupon / UserCoupon**: 쿠폰 발급/사용/소멸 관리
- **CouponPolicy**: 자동 발급 정책 인터페이스 + 구현체 3종

---

## 🧠 구현 포인트

- **JWT 기반 인증 및 Role 인가**
- **Redis를 이용한 장바구니 세션 및 병합 처리**
- **도메인 주도 설계 기반 상태 전이 (Order → Payment 연계)**
- **스케줄러 기반 포인트 만료 및 쿠폰 소멸 자동화**
- **쿠폰 발급 정책 확장 가능 구조 설계 (OCP 기반)**
- **프론트와의 API 통합 및 상태 관리(AuthContext, CartContext)**
- **통합 테스트 기반 개발 및 안정성 확보**

---

## ▶️ 실행 방법

### 백엔드
```bash
./gradlew clean build
java -jar build/libs/comerspleject-0.0.1-SNAPSHOT.jar
```

### 프론트엔드
```bash
npm install
npm run dev
```

### 배포
- **백엔드**: AWS EC2 (Ubuntu + MySQL + Redis)
- **프론트엔드**: AWS S3 정적 호스팅 (http://ecomos-frontend.s3-website.ap-northeast-2.amazonaws.com)
