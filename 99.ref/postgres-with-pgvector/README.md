# 99.ref/postgres-with-pgvector

이 디렉토리는 **pgvector가 포함된 PostgreSQL 로컬 실행 실습**을 위한 스크립트를 제공합니다.

## 이 디렉토리 기준 구성

- `pgvector-run.sh`: pgvector 컨테이너 실행(네트워크/볼륨 포함)
- `pg-run.sh.old`: 이전 실행 스크립트 백업
- `pgdata/`: 데이터 디렉토리(로컬 테스트용)

## 한 줄 요약

벡터 검색/임베딩 저장 실습을 위해 PostgreSQL+pgvector를 빠르게 실행하는 보조 코드입니다.
