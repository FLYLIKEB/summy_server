#!/bin/bash
set -euo pipefail

# 기본 설정값
SERVER_IP=""
SERVER_USER="ubuntu"
DEPLOY_PATH="/opt/summy_server"
SSH_KEY="summy.pem"
JAR_FILE="./api/build/libs/api-0.0.1-SNAPSHOT.jar"

# 서버 설정 파일이 있으면 해당 파일에서 설정을 로드
CONFIG_FILE="server-config.env"
if [ -f "$CONFIG_FILE" ]; then
  echo "서버 설정 파일 로드 중: $CONFIG_FILE"
  source $CONFIG_FILE
else
  echo "경고: 서버 설정 파일($CONFIG_FILE)이 없습니다. 명령줄 인자를 사용합니다."
fi

# 명령줄 인자가 있으면 해당 값으로 덮어씁니다
if [ $# -ge 1 ] && [ -n "$1" ]; then
  SERVER_IP="$1"
fi
if [ $# -ge 2 ] && [ -n "$2" ]; then
  SERVER_USER="$2"
fi
if [ $# -ge 3 ] && [ -n "$3" ]; then
  DEPLOY_PATH="$3"
fi

# 입력값 확인
if [ -z "$SERVER_IP" ]; then
  echo "오류: 서버 IP 주소가 지정되지 않았습니다."
  echo "사용법: ./remote-deploy.sh [서버_IP] [사용자_이름] [배포_경로]"
  echo "또는 $CONFIG_FILE 파일을 사용하여 서버 정보를 설정하세요."
  echo "예시:"
  echo "SERVER_IP=\"52.78.150.124\""
  echo "SERVER_USER=\"ubuntu\""
  echo "DEPLOY_PATH=\"/opt/summy_server\""
  echo "SSH_KEY=\"summy.pem\""
  exit 1
fi

echo "배포 정보:"
echo "서버: $SERVER_USER@$SERVER_IP"
echo "배포 경로: $DEPLOY_PATH"
echo "SSH 키: $SSH_KEY"

# JAR 파일 빌드
echo "===== 빌드 시작 ====="
./gradlew clean build
echo "빌드 완료"

# JAR 파일 존재 확인
if [ ! -f "$JAR_FILE" ]; then
  echo "오류: JAR 파일을 찾을 수 없습니다. 빌드가 실패했을 수 있습니다."
  exit 1
fi

echo "===== 원격 배포 시작 ====="

# 서버에 배포 디렉토리 생성
echo "배포 디렉토리 확인 중..."
ssh -i $SSH_KEY $SERVER_USER@$SERVER_IP "mkdir -p $DEPLOY_PATH" || {
  echo "배포 디렉토리 생성 실패"
  exit 1
}

# Docker 권한 설정 - Docker 소켓 파일에 권한 추가
echo "Docker 권한 설정 중..."
ssh -i $SSH_KEY $SERVER_USER@$SERVER_IP "sudo usermod -aG docker $SERVER_USER && sudo chmod 666 /var/run/docker.sock" || {
  echo "Docker 권한 설정 실패. 계속 진행합니다."
}

# JAR 파일 전송
echo "JAR 파일 전송 중..."
scp -i $SSH_KEY $JAR_FILE $SERVER_USER@$SERVER_IP:$DEPLOY_PATH/ || {
  echo "JAR 파일 전송 실패"
  exit 1
}

# compose.yaml 파일 전송
echo "Docker Compose 파일 전송 중..."
scp -i $SSH_KEY compose.yaml $SERVER_USER@$SERVER_IP:$DEPLOY_PATH/ || {
  echo "Docker Compose 파일 전송 실패"
  exit 1
}

# deploy.sh 스크립트 전송
echo "배포 스크립트 전송 중..."
scp -i $SSH_KEY deploy.sh $SERVER_USER@$SERVER_IP:$DEPLOY_PATH/ || {
  echo "배포 스크립트 전송 실패"
  exit 1
}

# 스크립트 실행 권한 부여 및 실행
echo "배포 스크립트 실행 중..."
ssh -i $SSH_KEY $SERVER_USER@$SERVER_IP "chmod +x $DEPLOY_PATH/deploy.sh && cd $DEPLOY_PATH && ./deploy.sh"

# 애플리케이션 실행 확인
echo "애플리케이션 실행 상태 확인 중..."
ssh -i $SSH_KEY $SERVER_USER@$SERVER_IP "ps aux | grep java | grep -v grep" && {
  echo "===== 배포 완료 ====="
  echo "애플리케이션이 $SERVER_IP:8080 에서 실행 중입니다."
} || {
  echo "===== 배포 실패 ====="
  echo "애플리케이션이 실행되지 않았습니다."
  exit 1
}