# 베이스 이미지로 OpenJDK 21 버전의 JRE 이미지 사용
FROM eclipse-temurin:21-jre-jammy

# 작업 디렉토리 설정
WORKDIR /app

# 로컬에서 빌드된 JAR 파일 복사
COPY target/spring-boot-app-0.0.1-SNAPSHOT.jar app.jar

# 외부에서 컨테이너의 8080 포트에 접근할 수 있도록 설정
EXPOSE 8080
EXPOSE 8081

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","app.jar"]
