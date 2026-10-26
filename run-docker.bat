@echo off
cd /d "%~dp0"
docker compose up --build --abort-on-container-exit --exit-code-from test-runner
set "TEST_EXIT=%ERRORLEVEL%"
docker compose down
exit /b %TEST_EXIT%
