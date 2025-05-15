# AWS EC2 서버 연결 및 배포 가이드

## 1. EC2 인스턴스 접속하기

### 1-1. SSH 키 준비

AWS EC2 인스턴스를 생성할 때 다운로드한 `.pem` 키 파일을 안전한 위치에 저장하세요.

```bash
# 키 파일 권한 변경 (Linux/macOS)
chmod 400 your-key.pem
```

### 1-2. SSH로 EC2 연결

```bash
# Amazon Linux/Ubuntu 인스턴스 접속
ssh -i your-key.pem ec2-user@your-ec2-instance-public-ip    # Amazon Linux
ssh -i your-key.pem ubuntu@your-ec2-instance-public-ip      # Ubuntu
```

## 2. 배포를 위한 EC2 환경 설정

### 2-1. Docker 설치

```bash
# Docker 설치 스크립트 다운로드
curl -O https://raw.githubusercontent.com/yourusername/summy_server/main/docker-install.sh

# 실행 권한 설정
chmod +x docker-install.sh

# 스크립트 실행
./docker-install.sh

# Docker가 정상적으로 설치되었는지 확인
docker --version
docker-compose --version
```

### 2-2. 배포 디렉토리 준비

```bash
# 배포 디렉토리 생성
sudo mkdir -p /opt/summy_server

# 권한 설정 (ec2-user 또는 ubuntu를 사용자 이름으로 변경)
sudo chown -R $(whoami):$(whoami) /opt/summy_server

# 배포 디렉토리로 이동
cd /opt/summy_server
```

## 3. GitHub Actions에서 사용할 배포 키 설정

### 3-1. 배포용 SSH 키 생성 (로컬 컴퓨터에서)

```bash
# SSH 키 생성
ssh-keygen -t rsa -b 4096 -C "your_email@example.com" -f ~/.ssh/ec2_deploy_key

# 공개 키 내용 확인 (이 내용을 EC2 서버에 등록해야 함)
cat ~/.ssh/ec2_deploy_key.pub

# 개인 키 내용 확인 (이 내용을 GitHub 시크릿으로 등록해야 함)
cat ~/.ssh/ec2_deploy_key
```

### 3-2. 공개 키를 EC2 서버에 등록 (EC2 서버에서)

```bash
# authorized_keys 파일에 공개 키 추가
mkdir -p ~/.ssh
echo "생성한 공개 키 내용 붙여넣기" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

## 4. GitHub 시크릿 설정

GitHub 리포지토리의 Settings > Secrets and variables > Actions에서 다음 시크릿을 추가하세요:

- `SSH_PRIVATE_KEY`: EC2 배포용 개인 키 내용 (ec2_deploy_key 파일 내용)
- `SERVER_IP`: EC2 인스턴스의 퍼블릭 IP 주소
- `SERVER_USER`: EC2 사용자 이름 (ec2-user 또는 ubuntu)
- `DEPLOY_PATH`: 배포 디렉토리 경로 (/opt/summy_server)
- `DOCKER_USERNAME`: Docker Hub 사용자 이름 (Docker Hub 사용 시)
- `DOCKER_PASSWORD`: Docker Hub 비밀번호 (Docker Hub 사용 시)

## 5. 보안 그룹 설정

EC2 인스턴스의 보안 그룹에서 다음 포트를 열어주세요:

- SSH (22): GitHub Actions에서 SSH 연결용
- HTTP (80): 웹 서비스 접근용
- HTTPS (443): 보안 웹 서비스 접근용
- 애플리케이션 포트 (8080): 직접 애플리케이션 접근용

## 6. 배포 테스트

GitHub 저장소의 main 브랜치에 코드를 푸시하면 CI/CD 파이프라인이 자동으로 실행됩니다.
GitHub의 Actions 탭에서 워크플로우 실행 상태를 확인할 수 있습니다.

## 7. 배포 확인 및 문제 해결

### 7-1. 배포 상태 확인 (EC2 서버에서)

```bash
# 배포 디렉토리로 이동
cd /opt/summy_server

# 배포 로그 확인
cat /var/log/summy_deploy.log

# Docker 컨테이너 상태 확인
docker ps

# Docker 컨테이너 로그 확인
docker logs <container_id>
```

### 7-2. 애플리케이션 접속 테스트

웹 브라우저에서 다음 주소로 접속하여 애플리케이션 동작을 확인합니다:
- http://your-ec2-instance-public-ip:8080

## 8. 도메인 연결 (선택사항)

1. Route 53 또는 외부 DNS 서비스에서 도메인을 EC2 인스턴스 IP로 연결
2. HTTPS 설정을 위해 Let's Encrypt로 SSL 인증서 발급

```bash
# Certbot 설치 (Ubuntu 기준)
sudo apt-get update
sudo apt-get install certbot
```

## 9. 자동 배포 오류 시 수동 배포 방법

EC2 서버에서 직접 빌드 및 실행:

```bash
# 코드 클론
git clone https://github.com/yourusername/summy_server.git

# 디렉토리 이동
cd summy_server

# Gradle 빌드
./gradlew build

# 앱 실행
java -jar */build/libs/*.jar
``` 