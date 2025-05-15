#!/bin/bash
set -euo pipefail

# 로그 파일 설정
LOG_FILE="docker_install.log"
echo "===== Docker 설치 시작: $(date) =====" >> $LOG_FILE

# 기존 Docker 패키지 제거
echo "기존 Docker 패키지 제거 중..." >> $LOG_FILE
sudo apt-get remove -y docker docker-engine docker.io containerd runc >> $LOG_FILE 2>&1

# 필수 패키지 설치
echo "필수 패키지 설치 중..." >> $LOG_FILE
sudo apt-get update >> $LOG_FILE 2>&1
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release >> $LOG_FILE 2>&1

# Docker의 공식 GPG 키 추가
echo "Docker 공식 GPG 키 추가 중..." >> $LOG_FILE
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg >> $LOG_FILE 2>&1

# Docker 레포지토리 설정
echo "Docker 레포지토리 설정 중..." >> $LOG_FILE
echo \
  "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker Engine 설치
echo "Docker Engine 설치 중..." >> $LOG_FILE
sudo apt-get update >> $LOG_FILE 2>&1
sudo apt-get install -y docker-ce docker-ce-cli containerd.io >> $LOG_FILE 2>&1

# Docker 서비스 시작 및 활성화
echo "Docker 서비스 시작 및 활성화 중..." >> $LOG_FILE
sudo systemctl start docker >> $LOG_FILE 2>&1
sudo systemctl enable docker >> $LOG_FILE 2>&1

# 현재 사용자를 docker 그룹에 추가
echo "현재 사용자를 docker 그룹에 추가 중..." >> $LOG_FILE
sudo usermod -aG docker ${SUDO_USER:-$USER} >> $LOG_FILE 2>&1

# Docker Compose 설치
echo "Docker Compose 설치 중..." >> $LOG_FILE
COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep 'tag_name' | cut -d\" -f4)
sudo curl -L "https://github.com/docker/compose/releases/download/${COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose >> $LOG_FILE 2>&1
sudo chmod +x /usr/local/bin/docker-compose >> $LOG_FILE 2>&1

# 설치 확인
echo "Docker 버전: $(docker --version)" >> $LOG_FILE
echo "Docker Compose 버전: $(docker-compose --version)" >> $LOG_FILE

echo "===== Docker 설치 완료: $(date) =====" >> $LOG_FILE
echo "Docker와 Docker Compose가 설치되었습니다."
echo "변경 사항을 적용하려면 시스템을 재시작하거나 다음 명령을 실행하세요:"
echo "newgrp docker" 