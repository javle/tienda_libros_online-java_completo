@echo off
where mvn >nul 2>nul
if %ERRORLEVEL%==0 (
  mvn %*
  exit /b %ERRORLEVEL%
)
echo No se encontro Maven en el sistema. Instalalo para usar este script.
exit /b 1
