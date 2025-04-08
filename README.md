# Summy Server

## 프로젝트 개요
Summy Server는 마이크로서비스 아키텍처를 기반으로 한 서버 애플리케이션입니다.

## 프로젝트 구조
```
summy_server/
├── api/           # API 엔드포인트 모듈
├── core/          # 핵심 비즈니스 로직 모듈
├── build.gradle   # 프로젝트 빌드 설정
└── compose.yaml   # Docker Compose 설정
```

## 기술 스택
- Java 21
- Gradle
- Docker
- Spring Boot 3.4

## 시작하기

### 요구사항
- JDK 21 이상
- Docker
- Gradle

### 빌드 및 실행
1. 프로젝트 클론
```bash
git clone [repository-url]
cd summy_server
```

2. 빌드
```bash
./gradlew build
```

3. Docker로 실행
```bash
docker-compose up
```

## 모듈 설명

### Core 모듈
핵심 비즈니스 로직을 포함하는 모듈입니다.

### API 모듈
REST API 엔드포인트를 제공하는 모듈입니다.

## 라이선스
[라이선스 정보]

## 기여 방법
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request 