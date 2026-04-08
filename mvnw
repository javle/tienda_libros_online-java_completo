#!/bin/sh
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi
echo "No se encontró Maven en el sistema. Instálalo para usar este script." >&2
exit 1
