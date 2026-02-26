# 01.frontend

이 디렉토리는 **기본형 웹 채팅 화면**으로 MCP 클라이언트 API를 호출해 대화를 확인하는 프론트엔드 실습 코드입니다.

## 이 디렉토리 기준 구성

- `index.html`: 채팅 화면과 입력 UI, `/api/chat` 호출 스크립트 포함
- `js/springai.js`: 사용자 질문/응답 출력, 스트리밍 텍스트 렌더링 로직
- `css/springai.css`: 기본 채팅 화면 스타일
- `image/`: UI 아이콘/로고 이미지
- `nginx.conf`: Nginx 정적 파일 서빙 설정
- `Dockerfile`: 프론트 정적 파일 컨테이너 이미지 빌드
- `docker-build.sh`, `docker-push.sh`: 이미지 빌드/푸시 스크립트
- `k8s/`: 프론트 배포용 Kubernetes 매니페스트 템플릿 및 결과 파일

## 한 줄 요약

가장 단순한 채팅 UI로 Spring AI MCP 백엔드 연동 흐름을 빠르게 확인하는 예제입니다.
