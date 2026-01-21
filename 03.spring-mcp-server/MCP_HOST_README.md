# MCP Host 프로젝트

## 개요

이 프로젝트는 Spring Boot 기반 MCP Server를 호출하는 독립적인 MCP Host 애플리케이션입니다.

## 프로젝트 구조

```
springai-mcp-server/
├── src/                          # MCP Server (기존)
│   └── main/java/com/skala/springbootsample/
│       ├── mcp/                  # MCP Tools (@Tool 메서드들)
│       ├── controller/           # REST Controller
│       └── config/               # MCP Server 설정
│
└── mcp-host/                     # MCP Client Host (새로 생성)
    ├── src/main/java/com/skala/mcphost/
    │   ├── McpHostApplication.java
    │   ├── config/
    │   │   └── McpClientConfig.java       # MCP Client 설정
    │   ├── service/
    │   │   └── McpToolService.java        # MCP Tool 호출 로직
    │   └── controller/
    │       └── McpHostController.java     # REST API 제공
    ├── src/main/resources/
    │   └── application.yaml               # 설정 파일
    ├── pom.xml
    ├── README.md
    └── test-api.sh                        # API 테스트 스크립트
```

## 실행 방법

### 1. MCP Server 실행

메인 프로젝트에서 MCP Server를 실행합니다:

```bash
# MCP Server (포트 8080)
./mvnw spring-boot:run
```

### 2. MCP Host 실행

별도 터미널에서 MCP Host를 실행합니다:

```bash
# MCP Host (포트 8081)
cd mcp-host
../mvnw spring-boot:run
```

## MCP Host API 엔드포인트

| 엔드포인트 | 메서드 | 설명 |
|----------|--------|------|
| `/api/mcp/health` | GET | Health Check |
| `/api/mcp/initialize` | POST | MCP Server 초기화 및 도구 목록 조회 |
| `/api/mcp/tool/system-status` | POST | 시스템 상태 조회 |
| `/api/mcp/tool/users?name=...` | POST | 사용자 목록 조회 (선택적 필터) |
| `/api/mcp/tool/regions` | POST | 지역 목록 조회 |

## 테스트 방법

### 방법 1: 테스트 스크립트 사용

```bash
cd mcp-host
./test-api.sh
```

### 방법 2: cURL 사용

```bash
# Health Check
curl http://localhost:8081/api/mcp/health

# 초기화 및 도구 목록 조회
curl -X POST http://localhost:8081/api/mcp/initialize

# 시스템 상태 조회
curl -X POST http://localhost:8081/api/mcp/tool/system-status

# 사용자 목록 조회
curl -X POST http://localhost:8081/api/mcp/tool/users

# 지역 목록 조회
curl -X POST http://localhost:8081/api/mcp/tool/regions
```

### 방법 3: 브라우저

```
http://localhost:8081/api/mcp/health
```

## 설정

`mcp-host/src/main/resources/application.yaml` 파일에서 MCP Server URL을 설정할 수 있습니다:

```yaml
mcp:
  server:
    url: http://localhost:8080/mcp
```

## 동작 원리

1. **MCP Host** (`localhost:8081`)가 **MCP Server** (`localhost:8080`)에 HTTP 요청을 보냅니다.
2. 요청은 MCP 프로토콜 (JSON-RPC 2.0) 형식으로 전송됩니다.
3. MCP Server는 `@Tool` 어노테이션이 붙은 메서드를 실행합니다.
4. 결과를 JSON-RPC 형식으로 반환합니다.

## 예제 요청/응답

### 초기화 요청

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {
    "protocolVersion": "2024-11-05",
    "capabilities": {},
    "clientInfo": {
      "name": "spring-boot-mcp-host",
      "version": "1.0.0"
    }
  }
}
```

### Tool 호출 요청

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "getSystemStatus",
    "arguments": {}
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "시스템 상태:\n- 총 사용자 수: 5명\n- 총 지역 수: 3개\n- 서버 상태: 정상\n- 현재 시간: 2024-01-15T10:30:00"
      }
    ]
  }
}
```

## 의존성

MCP Host는 다음 주요 의존성을 사용합니다:

- **Spring Boot 3.4.3**: 웹 애플리케이션 프레임워크
- **Spring Web**: HTTP 클라이언트 (RestTemplate)
- **Lombok**: 코드 생성 도구

MCP Server와 독립적으로 실행되므로, MCP Server의 Spring AI MCP 의존성이 필요 없습니다.

## 장점

1. **독립적인 실행**: MCP Server와 MCP Host를 독립적으로 실행/테스트 가능
2. **명확한 역할 분리**: 서버와 클라이언트가 명확히 분리됨
3. **유연한 확장**: MCP Host를 다른 MCP Server에 연결하거나 여러 서버를 관리 가능
4. **REST API 제공**: MCP Host가 REST API를 제공하여 간단한 통합 가능

## 문제 해결

### MCP Server에 연결할 수 없음

- MCP Server가 실행 중인지 확인 (`http://localhost:8080/actuator/health`)
- `application.yaml`의 `mcp.server.url` 설정 확인
- 방화벽 또는 네트워크 설정 확인

### Tool 호출 실패

- MCP Server 로그 확인
- JSON-RPC 형식이 올바른지 확인
- Tool 이름이 정확한지 확인 (대소문자 구분)

## 추가 개발

MCP Host에 새로운 기능을 추가하려면:

1. `McpToolService`에 새로운 메서드 추가
2. `McpHostController`에 새로운 엔드포인트 추가
3. 필요한 경우 설정 추가 (`application.yaml`)

