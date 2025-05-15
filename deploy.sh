#!/bin/bash
set -euo pipefail

# 로그 파일 설정
LOG_FILE="./summy_deploy.log"
APP_NAME="summy_server"
JAR_PATH=$(find . -name "*.jar" | sort -V | tail -n 1)
PID_FILE="./summy_server.pid"
ENV_FILE=".env"

# 현재 시간을 로그에 기록
echo "===== 배포 시작: $(date) =====" >> $LOG_FILE

# 환경 변수 파일 확인
if [ -f "$ENV_FILE" ]; then
  echo "환경 변수 파일(.env)이 있습니다. 애플리케이션에 환경 변수를 제공합니다." >> $LOG_FILE
  
  # 환경 변수 로드
  set -a
  source $ENV_FILE
  set +a
else
  echo "경고: 환경 변수 파일(.env)이 없습니다. 기본값을 사용합니다." >> $LOG_FILE
fi

# 애플리케이션이 실행 중인지 확인하고 중지
if [ -f "$PID_FILE" ]; then
  PID=$(cat $PID_FILE)
  if ps -p $PID > /dev/null; then
    echo "기존 애플리케이션 중지 (PID: $PID)" >> $LOG_FILE
    kill $PID
    sleep 10
    
    # 여전히 실행 중이라면 강제 종료
    if ps -p $PID > /dev/null; then
      echo "애플리케이션이 여전히 실행 중입니다. 강제 종료합니다." >> $LOG_FILE
      kill -9 $PID
      sleep 5
    fi
  else
    echo "PID 파일이 존재하지만, 해당 프로세스가 실행 중이 아닙니다." >> $LOG_FILE
  fi
  rm -f $PID_FILE
fi

# 새 JAR 파일 확인
if [ -z "$JAR_PATH" ]; then
  echo "오류: JAR 파일을 찾을 수 없습니다!" >> $LOG_FILE
  exit 1
fi

echo "배포할 JAR 파일: $JAR_PATH" >> $LOG_FILE

# MariaDB 실행 확인
if ! docker ps | grep -q mariadb; then
  echo "MariaDB 컨테이너 시작" >> $LOG_FILE
  docker-compose -f compose.yaml up -d mariadb
  sleep 10
fi

# Redis 실행 확인
if ! docker ps | grep -q redis; then
  echo "Redis 컨테이너 시작" >> $LOG_FILE
  docker-compose -f compose.yaml up -d redis
  sleep 5
fi

# 새 버전 실행 (환경 변수 파일 사용)
echo "새 애플리케이션 버전 시작 중..." >> $LOG_FILE

# 환경 변수 파일이 있는 경우, 모든 환경 변수를 내보내서 애플리케이션에서 사용할 수 있게 함
if [ -f "$ENV_FILE" ]; then
  # 환경 변수 파일의 내용을 로그에 기록 (API 키는 마스킹)
  echo "환경 변수 설정:" >> $LOG_FILE
  cat $ENV_FILE | sed 's/\(XAI_API_KEY=\).*/\1********/g' >> $LOG_FILE
  
  # 환경 변수 파일을 적용하여 자바 애플리케이션 실행
  nohup bash -c "source $ENV_FILE && java -jar $JAR_PATH \
    --spring.profiles.active=prod \
    --server.port=8080 \
    --spring.config.import=optional:file:.env[.properties] \
    > ./summy_server.log 2>&1" &
else
  # 환경 변수 없이 기본 설정으로 실행
  nohup java -jar $JAR_PATH \
    --spring.profiles.active=prod \
    --server.port=8080 \
    > ./summy_server.log 2>&1 &
fi

# PID 저장
echo $! > $PID_FILE
echo "애플리케이션이 시작되었습니다. PID: $(cat $PID_FILE)" >> $LOG_FILE

# 실행 상태 확인
sleep 10
if ps -p $(cat $PID_FILE) > /dev/null; then
  echo "애플리케이션이 성공적으로 시작되었습니다!" >> $LOG_FILE
  echo "===== 배포 완료: $(date) =====" >> $LOG_FILE
else
  echo "오류: 애플리케이션 시작에 실패했습니다!" >> $LOG_FILE
  echo "로그를 확인하세요: ./summy_server.log" >> $LOG_FILE
  echo "===== 배포 실패: $(date) =====" >> $LOG_FILE
  exit 1
fi 