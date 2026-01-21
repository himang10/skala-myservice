# 베이스 이미지로 OpenJDK 21 버전의 JRE 이미지 사용
FROM eclipse-temurin:21-jre-jammy

# 작업 디렉토리 설정
WORKDIR /app

# 로컬에서 빌드된 JAR 파일 복사
COPY target/mcp-client-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# 환경변수 기본값 설정 (docker-compose에서 오버라이드 가능)
ENV OPEN_AI_KEY=""
ENV SPRING_PROFILES_ACTIVE=mariadb

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
