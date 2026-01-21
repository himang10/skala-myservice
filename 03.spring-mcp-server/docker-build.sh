#!/bin/bash
NAME=sk000
IMAGE_NAME="spring-mcp-server"
VERSION="1.0.0"

ARCH=amd64
#ARCH=arm64

# CPU 플랫폼 suffix 설정
if [ "$ARCH" = "arm64" ]; then
  CPU_PLATFORM=".arm64"
else
  CPU_PLATFORM=""
fi

# Docker 이미지 빌드
docker build \
  --tag ${NAME}-${IMAGE_NAME}${CPU_PLATFORM}:${VERSION} \
  --file Dockerfile \
  --platform linux/${ARCH} \
  ${IS_CACHE} .