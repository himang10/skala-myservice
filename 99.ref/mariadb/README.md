# 99.ref/mariadb

이 디렉토리는 **로컬 MariaDB(CDC 설정 포함) 단독 실행 실습**을 위한 스크립트 모음입니다.

## 이 디렉토리 기준 구성

- `run.sh`: MariaDB 컨테이너 실행(네트워크/볼륨/CDC 설정 포함)
- `stop.sh`: 실행 중 컨테이너 중지
- `logs.sh`: 컨테이너 로그 확인
- `clean.sh`: 컨테이너/볼륨 정리

## 한 줄 요약

Docker Compose 없이 MariaDB만 별도로 띄워 테스트할 때 사용하는 레퍼런스 스크립트입니다.
