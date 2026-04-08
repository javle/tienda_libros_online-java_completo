#!/bin/bash
set -e

docker compose up -d

if [ -x "./mvnw" ]; then
  ./mvnw spring-boot:run
elif command -v mvn >/dev/null 2>&1; then
  mvn spring-boot:run
else
  echo "No se encontró Maven. Instálalo o usa un IDE con soporte Maven."
  exit 1
fi
