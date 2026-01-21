# Docker Compose 사용 가이드

## 준비사항

### 1. 환경 변수 설정
```bash
# .env 파일 생성
cp .env.example .env

# .env 파일을 편집하여 OPEN_AI_KEY 입력
vi .env
```

### 2. Docker 및 Docker Compose 설치 확인
```bash
docker --version
docker-compose --version
```

## spring boot 사전 컴파일
```bash
cd 02.mcp-client
mvn clean install -DskiptTests

cd ../03.spring-mcp-server
mvn clean install -DskipTests

cd ..
```

## 실행 방법

### 전체 서비스 시작 (빌드 포함)
```bash
# 백그라운드에서 실행 (선택1)
docker-compose up -d --build

# 로그 확인하면서 실행
docker-compose up --build
```

### 개별 서비스 시작 (선택2)
```bash
# MariaDB만 시작
docker-compose up -d mariadb

# Spring MCP Server만 시작 (MariaDB 의존성 자동 시작)
docker-compose up -d spring-mcp-server

# MCP Client만 시작 (모든 의존성 자동 시작)
docker-compose up -d mcp-client

# Frontend만 시작 (모든 의존성 자동 시작)
docker-compose up -d frontend

```

## 서비스 관리

### 로그 확인
```bash
# 전체 로그
docker-compose logs -f

# 특정 서비스 로그
docker-compose logs -f mcp-client
docker-compose logs -f spring-mcp-server
docker-compose logs -f mariadb
docker-compose logs -f frontend
```

### 서비스 중지
```bash
# 전체 서비스 중지
docker-compose down

# 볼륨까지 삭제 (데이터베이스 데이터 삭제)
docker-compose down -v
```

### 서비스 재시작
```bash
# 전체 재시작
docker-compose restart

# 특정 서비스 재시작
docker-compose restart mcp-client
```

### 빌드 없이 시작 (이미지가 이미 빌드된 경우)
```bash
docker-compose up -d
```

## 서비스 접속

- **MCP Client**: http://localhost:8080
- **Spring MCP Server**: http://localhost:8081
- **MariaDB**: localhost:3306
  - Database: `cloud`
  - User: `skala`
  - Password: `Skala25a!23$`

## 네트워크 구조

모든 서비스는 `app-network`라는 동일한 브리지 네트워크에서 실행됩니다:
- 서비스 간 통신은 컨테이너 이름으로 가능
- 예: mcp-client → spring-mcp-server (http://spring-mcp-server:8080)
- 예: spring-mcp-server → mariadb (jdbc:mariadb://mariadb:3306/cloud)

## 헬스체크

각 서비스는 헬스체크 기능이 포함되어 있습니다:
- **MariaDB**: 10초마다 healthcheck.sh 실행
- **Spring MCP Server**: 10초마다 /actuator/health 확인
- **MCP Client**: 10초마다 /actuator/health 확인

헬스체크 상태 확인:
```bash
docker-compose ps
```

## 트러블슈팅

### 빌드 실패 시
```bash
# 캐시 없이 다시 빌드
docker-compose build --no-cache

# 특정 서비스만 재빌드
docker-compose build --no-cache mcp-client
```

### 포트 충돌 시
docker-compose.yaml의 ports 섹션을 수정:
```yaml
ports:
  - "18080:8080"  # 호스트:컨테이너
```

### 데이터베이스 초기화
```bash
# 볼륨 삭제 후 재시작
docker-compose down -v
docker-compose up -d
```

### 네트워크 문제
```bash
# 네트워크 재생성
docker-compose down
docker network prune
docker-compose up -d
```

## 개발 워크플로우

### 코드 수정 후 재배포
```bash
# 1. 특정 서비스만 재빌드 및 재시작
docker-compose up -d --build mcp-client

# 2. 또는 중지 후 재빌드
docker-compose stop mcp-client
docker-compose build mcp-client
docker-compose up -d mcp-client
```

### 환경 변수 변경 시
```bash
# .env 파일 수정 후
docker-compose up -d --force-recreate
```
