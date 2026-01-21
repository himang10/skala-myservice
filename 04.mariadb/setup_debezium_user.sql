-- Debezium CDC 전용 사용자 생성
CREATE USER IF NOT EXISTS 'skala'@'%' IDENTIFIED BY 'Skala25a!23$';

-- Debezium 필수 권한 부여
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT
ON *.* TO 'skala'@'%';

-- 특정 데이터베이스에 대한 추가 권한
GRANT ALL PRIVILEGES ON cloud.* TO 'skala'@'%';

-- 변경사항 적용
FLUSH PRIVILEGES;
