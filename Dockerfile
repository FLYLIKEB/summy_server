FROM openjdk:21-slim

WORKDIR /app

# 애플리케이션 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 배포 스크립트 복사
COPY deploy.sh /app/deploy.sh
RUN chmod +x /app/deploy.sh

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"] 