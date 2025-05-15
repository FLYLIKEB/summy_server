#!/bin/bash
set -euo pipefail

# 이 스크립트는 EC2 서버 연결 및 GitHub Actions 배포를 위한 SSH 키를 설정합니다

# 사용자 입력 받기
read -p "EC2 인스턴스 공개 IP 주소를 입력하세요: " EC2_IP
read -p "EC2 사용자 이름을 입력하세요 (예: ec2-user 또는 ubuntu): " EC2_USER
read -p "EC2 인스턴스 PEM 키 파일 경로를 입력하세요: " PEM_KEY
read -p "GitHub 저장소 이름을 입력하세요 (예: username/repo): " GITHUB_REPO

# PEM 키 권한 설정
chmod 400 "$PEM_KEY"
echo "PEM 키 권한이 설정되었습니다."

# EC2 연결 테스트
echo "EC2 연결을 테스트합니다..."
ssh -i "$PEM_KEY" -o StrictHostKeyChecking=no $EC2_USER@$EC2_IP "echo '연결 성공!'"
if [ $? -ne 0 ]; then
  echo "EC2 연결에 실패했습니다. PEM 키와 IP 주소를 확인하세요."
  exit 1
fi

# GitHub Actions 배포용 키 생성
echo "GitHub Actions 배포용 SSH 키를 생성합니다..."
SSH_KEY_PATH="$HOME/.ssh/ec2_deploy_key"
ssh-keygen -t rsa -b 4096 -C "deploy@github-actions" -f "$SSH_KEY_PATH" -N ""
echo "SSH 키가 생성되었습니다: $SSH_KEY_PATH"

# EC2 서버에 공개 키 복사
echo "EC2 서버에 공개 키를 복사합니다..."
PUB_KEY=$(cat "$SSH_KEY_PATH.pub")
ssh -i "$PEM_KEY" $EC2_USER@$EC2_IP "mkdir -p ~/.ssh && echo '$PUB_KEY' >> ~/.ssh/authorized_keys && chmod 600 ~/.ssh/authorized_keys"
if [ $? -ne 0 ]; then
  echo "공개 키 복사에 실패했습니다."
  exit 1
fi
echo "공개 키가 EC2 서버에 복사되었습니다."

# GitHub Actions 설정 안내
PRIVATE_KEY=$(cat "$SSH_KEY_PATH")
echo "===================== GitHub 시크릿 설정 안내 ====================="
echo "GitHub 저장소($GITHUB_REPO)의 Settings > Secrets and variables > Actions에"
echo "다음 시크릿을 추가하세요:"
echo ""
echo "SSH_PRIVATE_KEY:"
echo "$PRIVATE_KEY"
echo ""
echo "SERVER_IP:"
echo "$EC2_IP"
echo ""
echo "SERVER_USER:"
echo "$EC2_USER"
echo ""
echo "DEPLOY_PATH:"
echo "/opt/summy_server (또는 원하는 배포 경로)"
echo "=================================================================="

# EC2 연결 명령어 안내
echo "다음 명령어로 EC2 서버에 접속할 수 있습니다:"
echo "ssh -i $PEM_KEY $EC2_USER@$EC2_IP"
echo ""
echo "배포용 키로도 접속할 수 있습니다:"
echo "ssh -i $SSH_KEY_PATH $EC2_USER@$EC2_IP" 