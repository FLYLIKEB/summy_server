# Summy Server

## 프로젝트 개요
Summy Server는 클린 아키텍처와 멀티 모듈 구조를 기반으로 한 서버 애플리케이션입니다. CQRS 패턴과 Spring Data JPA, QueryDSL을 활용한 강력한 데이터 처리 기능을 제공합니다.

## 프로젝트 구조
```
summy_server/
├── api/                 # API 엔드포인트 모듈
│   └── src/
│       └── test/
│           └── java/
│               ├── unit/           # API 단위 테스트
│               └── integration/    # API 통합 테스트
│
├── core/                # 핵심 비즈니스 로직 모듈
│   └── src/
│       ├── main/java/
│       │   ├── config/        # 설정 클래스
│       │   ├── domain/        # 도메인 모델
│       │   ├── repository/    # 데이터 접근 계층
│       │   └── service/       # 비즈니스 로직
│       └── test/
│           └── java/
│               ├── unit/           # 단위 테스트
│               └── integration/    # 통합 테스트
│
├── build.gradle         # 루트 Gradle 빌드 설정
├── settings.gradle      # Gradle 설정
└── compose.yaml         # Docker Compose 설정
```

## 기술 스택
- Java 21
- Spring Boot 3.4
- Spring Data JPA
- QueryDSL
- Redis
- Flyway
- MariaDB
- JUnit 5 & AssertJ
- Gradle (멀티 모듈)
- Docker

## 주요 특징
- 클린 코드와 SOLID 원칙 준수
- 멀티 모듈 아키텍처
- CQRS 패턴 적용
- 단위 테스트 및 통합 테스트 분리
- JPA와 QueryDSL을 활용한 강력한 쿼리 기능
- 정적 팩토리 메서드 패턴 활용
- 도메인 주도 설계(DDD) 적용

## 시작하기

### 요구사항
- JDK 21 이상
- Docker & Docker Compose
- Gradle 8.0 이상

### 빌드 및 실행
1. 프로젝트 클론
```bash
git clone https://github.com/FLYLIKEB/summy_server.git
cd summy_server
```

2. 빌드
```bash
./gradlew clean build
```

3. Docker로 실행
```bash
docker-compose up -d
```

## 모듈 설명

### Core 모듈
핵심 비즈니스 로직, 도메인 모델 및 데이터 액세스 계층을 포함합니다.
- 도메인 엔티티 (User 등)
- JPA 및 QueryDSL 리포지토리
- 비즈니스 서비스
- 도메인 이벤트

### API 모듈
RESTful API 엔드포인트를 제공하고 HTTP 요청을 처리합니다.
- 컨트롤러
- DTO (Data Transfer Objects)
- 요청/응답 매핑
- 보안 및 인증 처리

## 테스트 실행
```bash
# 모든 테스트 실행
./gradlew test

# 단위 테스트만 실행
./gradlew test --tests "*.unit.*"

# 통합 테스트만 실행
./gradlew test --tests "*.integration.*"
```

## 라이선스
MIT License

## 기여 방법
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request 