# CI/CD 환경 설정 가이드

이 문서는 GitHub Actions를 사용한 CI/CD 파이프라인 설정 방법을 설명합니다.

## 1. GitHub Actions 설정

### 필요한 시크릿 설정

GitHub 리포지토리의 Settings > Secrets and variables > Actions에서 다음 시크릿을 추가해야 합니다:

- `SSH_PRIVATE_KEY`: 서버 접속용 SSH 비밀키
- `SERVER_IP`: 배포 서버 IP 주소
- `SERVER_USER`: 서버 접속 계정
- `DEPLOY_PATH`: 서버에서 애플리케이션을 배포할 경로
- `DOCKER_USERNAME`: Docker Hub 사용자 이름
- `DOCKER_PASSWORD`: Docker Hub 비밀번호

### SSH 키 생성 방법

서버 접속을 위한 SSH 키를 생성하는 방법:

```bash
# SSH 키 생성
ssh-keygen -t rsa -b 4096 -C "your_email@example.com" -f ~/.ssh/deploy_key

# 공개 키를 서버에 등록
ssh-copy-id -i ~/.ssh/deploy_key.pub user@your-server-ip

# 개인 키를 GitHub 시크릿에 등록 (내용 복사)
cat ~/.ssh/deploy_key
```

## 2. 서버 환경 설정

### Docker 설치

#### Ubuntu/Debian 서버

```bash
# 스크립트 다운로드
curl -O https://raw.githubusercontent.com/yourusername/summy_server/main/docker-install.sh

# 실행 권한 부여
chmod +x docker-install.sh

# 스크립트 실행
./docker-install.sh
```

#### macOS

```bash
# 스크립트 다운로드
curl -O https://raw.githubusercontent.com/yourusername/summy_server/main/docker-install-mac.sh

# 실행 권한 부여
chmod +x docker-install-mac.sh

# 스크립트 실행
./docker-install-mac.sh
```

### 배포 디렉토리 준비

```bash
# 배포 디렉토리 생성
mkdir -p /path/to/deploy/directory

# 권한 설정
sudo chown -R your-user:your-group /path/to/deploy/directory
```

## 3. CI/CD 파이프라인 구성 요소

### 빌드 및 테스트 (CI)

GitHub Actions 워크플로우의 `build` 작업에서 다음 단계를 수행합니다:

1. 코드 체크아웃
2. JDK 설정
3. Gradle 빌드
4. 단위, 통합, 인수 테스트 실행
5. 빌드 결과물 저장

### SSH를 통한 배포 (CD)

`deploy-ssh` 작업에서 다음 단계를 수행합니다:

1. 빌드 결과물 다운로드
2. SSH 비밀키 설정
3. 서버 호스트키 추가
4. JAR 파일 전송 및 배포 스크립트 실행

### Docker를 통한 배포 (CD)

`deploy-docker` 작업에서 다음 단계를 수행합니다:

1. 빌드 결과물 다운로드
2. Docker Hub 로그인
3. Docker 이미지 빌드 및 푸시
4. SSH를 통한 원격 배포 (Docker Compose 실행)

## 4. 배포 스크립트 (deploy.sh)

서버에서 애플리케이션을 실행하는 스크립트로 다음 기능을 수행합니다:

1. 기존 애플리케이션 프로세스 중지
2. JAR 파일 확인
3. MariaDB 및 Redis 컨테이너 실행 확인
4. 새 버전 애플리케이션 실행
5. 실행 상태 확인 및 로깅

## 5. 트러블슈팅

### 일반적인 문제

- **SSH 접속 오류**: SSH 키가 올바르게 설정되었는지 확인
- **권한 문제**: 배포 디렉토리 및 실행 스크립트의 권한 확인
- **Docker 실행 오류**: Docker 서비스가 실행 중인지 확인
- **애플리케이션 시작 실패**: 로그 파일 확인 (/var/log/summy_deploy.log)

### 로그 확인 방법

```bash
# 배포 로그 확인
cat /var/log/summy_deploy.log

# 애플리케이션 로그 확인
cat /var/log/summy_server.log

# Docker 컨테이너 로그 확인
docker logs <container_id>
```

## 6. 환경 변수 설정

프로덕션 환경에서는 다음 환경 변수를 설정하는 것이 좋습니다:

- `SPRING_PROFILES_ACTIVE`: 활성 프로필 (prod)
- `DB_PASSWORD`: 데이터베이스 비밀번호
- `SPRING_DATASOURCE_URL`: 데이터베이스 URL
- `SERVER_PORT`: 애플리케이션 포트 (기본 8080)

### 환경 변수 설정 방법

```bash
# 시스템 환경 변수 설정
echo 'export DB_PASSWORD="your-secure-password"' >> ~/.bashrc

# 애플리케이션 실행 시 환경 변수 전달
java -jar app.jar --DB_PASSWORD=your-secure-password
```

## 7. 보안 고려사항

- 민감한 정보(비밀번호, API 키 등)는 항상 환경 변수나 시크릿으로 관리
- SSH 키는 강력한 비밀번호로 보호하고 정기적으로 교체
- 서버 방화벽 설정을 통해 필요한 포트만 개방
- Docker 이미지는 정기적으로 업데이트하여 보안 취약점 패치 