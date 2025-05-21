# solopleject
🛒 E-Commerce 프로젝트 API 명세서

본 프로젝트는 Java + Spring Boot 기반으로 개발된 커머스 백엔드 시스템입니다. 사용자, 상품, 주문, 결제 등 주요 기능을 API 형태로 제공합니다.

📌 목차

사용자 기능 (회원)

상품 기능

장바구니 기능

주문 기능

결제 기능

배송 기능

리뷰 기능 (선택)

관리자 기능

✅ 사용자 기능 (회원)

회원가입

[POST] /api/users/signup

설명: 회원가입 처리

Request Body

{
  "email": "user@example.com",
  "password": "1234pass",
  "name": "홍길동",
  "role": "USER"
}

Response

{
  "userId": 1,
  "message": "회원가입 성공"
}

로그인 (JWT 발급)

[POST] /api/users/login

설명: 로그인 처리 및 토큰 발급

Request Body

{
  "email": "user@example.com",
  "password": "1234pass"
}

Response

{
  "accessToken": "JWT_ACCESS",
  "refreshToken": "JWT_REFRESH"
}

회원탈퇴

[DELETE] /api/users

회원정보 수정

[PUT] /api/users/info

비밀번호 재설정

[POST] /api/users/reset-password

권한 구분

USER / SELLER / ADMIN (JWT Claim 기반 권한 분기)

🛍️ 상품 기능

상품 등록

[POST] /api/products

상품 수정

[PUT] /api/products/{productId}

상품 삭제

[DELETE] /api/products/{productId}

상품 목록 조회

[GET] /api/products?category=전자기기&sort=price_asc

상품 상세 조회

[GET] /api/products/{productId}

재고 관리 (내부 처리 or 관리자 전용)

🛒 장바구니 기능

상품 추가

[POST] /api/cart

상품 삭제

[DELETE] /api/cart/{itemId}

수량 변경

[PUT] /api/cart/{itemId}

장바구니 전체 조회

[GET] /api/cart

장바구니 → 주문으로 이동

[POST] /api/orders/from-cart

💳 주문 기능

주문 생성

[POST] /api/orders

주문 내역 조회

[GET] /api/orders

주문 취소

[PUT] /api/orders/{orderId}/cancel

배송 상태 조회

[GET] /api/orders/{orderId}/status

💰 결제 기능

결제 요청

[POST] /api/payments

결제 성공/실패 처리 (Webhook or 내부 로직)

[POST] /api/payments/result

주문 상태 변경

결제 성공 시 주문 상태 결제완료로 변경

📦 배송 기능

배송지 등록

[POST] /api/delivery-address

배송지 선택

[PUT] /api/delivery-address/{id}

운송장 등록/조회

[GET] /api/orders/{orderId}/tracking

✍️ 리뷰 기능 (선택)

리뷰 작성

[POST] /api/reviews

리뷰 목록 조회

[GET] /api/products/{productId}/reviews

리뷰 삭제

[DELETE] /api/reviews/{reviewId}

🧑‍💻 관리자 기능

회원 관리

[GET] /api/admin/users

[PUT] /api/admin/users/{userId}/role

상품 관리

[GET] /api/admin/products

주문 및 결제 내역 확인

[GET] /api/admin/orders

통계 조회 (판매량 등)

[GET] /api/admin/statistics

📎 기타

모든 인증 필요한 API는 JWT 기반 인증 사용

accessToken은 Authorization: Bearer {token} 헤더로 전달

권한 분리는 Spring Security + Role 기반 인가 처리

일부 예시 응답은 생략했으며, Swagger 또는 Postman 문서로도 제공 예정
