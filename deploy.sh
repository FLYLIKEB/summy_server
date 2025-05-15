#!/bin/bash

# 로그 파일 설정
LOG_FILE="/var/log/summy_deploy.log"
APP_NAME="summy_server"
JAR_PATH=$(find . -name "*.jar" | sort -V | tail -n 1)
PID_FILE="/var/run/${APP_NAME}.pid"

# 현재 시간을 로그에 기록
echo "===== 배포 시작: $(date) =====" >> $LOG_FILE

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

# 새 버전 실행
echo "새 애플리케이션 버전 시작 중..." >> $LOG_FILE
nohup java -jar $JAR_PATH \
  --spring.profiles.active=prod \
  --server.port=8080 \
  > /var/log/${APP_NAME}.log 2>&1 &

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
  echo "로그를 확인하세요: /var/log/${APP_NAME}.log" >> $LOG_FILE
  echo "===== 배포 실패: $(date) =====" >> $LOG_FILE
  exit 1
fi 