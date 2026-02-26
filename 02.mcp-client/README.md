# 02.mcp-client

이 디렉토리는 **Spring AI MCP 클라이언트** 실습 코드로, 사용자의 질문을 받아 MCP 서버 도구 호출과 LLM 응답을 연결합니다.

## 이 디렉토리 기준 구성

- `src/main/java/com/example/demo/controller/`: 웹 요청 처리(`AiController`, `HomeController`)
- `src/main/java/com/example/demo/service/AiService.java`: Spring AI ChatClient 기반 대화 처리 핵심 로직
- `src/main/java/com/example/demo/config/ChatMemoryConfig.java`: 대화 메모리/클라이언트 설정
- `src/main/resources/application.yaml`: OpenAI 키, MCP 서버 연결, 포트/로깅 설정
- `src/test/`: 기본 테스트 코드
- `pom.xml`, `build.gradle`: Maven/Gradle 빌드 설정
- `Dockerfile`: 클라이언트 서비스 컨테이너 이미지 빌드
- `k8s/`: 클라이언트 배포용 Kubernetes 템플릿/매니페스트

## 한 줄 요약

프론트엔드 질문을 받아 MCP 서버 도구를 활용해 답변을 생성하는 "중간 오케스트레이터" 역할의 실습 코드입니다.
