# 03.spring-mcp-server

이 디렉토리는 **MCP 도구를 제공하는 Spring Boot 서버** 실습 코드입니다.

## 이 디렉토리 기준 구성

- `src/main/java/com/skala/springbootsample/mcp/`: MCP 도구 구현(`UserMcpTools`, `RegionMcpTools`, `WeatherTools` 등)
- `src/main/java/com/skala/springbootsample/controller/`: REST/헬스/보조 API 컨트롤러
- `src/main/java/com/skala/springbootsample/service/`: 비즈니스 서비스 계층
- `src/main/java/com/skala/springbootsample/repo/`: JPA Repository 계층
- `src/main/java/com/skala/springbootsample/domain/`: 엔티티 모델
- `src/main/resources/`: 프로파일/DB/애플리케이션 설정
- `pom.xml`: Spring Boot + Spring AI MCP 서버 의존성 설정
- `Dockerfile`: 서버 이미지 빌드
- `k8s/`, `kustomize/`: Kubernetes 배포 설정

## 한 줄 요약

MCP 클라이언트가 호출할 수 있는 도구(User/Region/시스템/날씨 등)를 제공하는 백엔드 실습 서버입니다.
