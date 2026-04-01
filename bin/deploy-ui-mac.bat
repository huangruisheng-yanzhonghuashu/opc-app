@echo off
chcp 65001 >nul
echo ========================================
echo OPC Admin UI Deploy Script
echo Target: 192.168.109
echo ========================================
echo.

set REMOTE_HOST=192.168.109
set REMOTE_USER=sevilinma
set REMOTE_DIR=/Users/sevilinma/opc

REM Get script directory and find project root
set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..
set UI_DIR=%PROJECT_ROOT%\opc-admin-ui
set DIST_DIR=%UI_DIR%\dist

echo UI directory: %UI_DIR%
echo Remote target: %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%/dist
echo.


echo.

if not exist "%DIST_DIR%" (
    echo [ERROR] Dist directory not found: %DIST_DIR%
    pause
    exit /b 1
)

echo [Step 2/3] Uploading dist to server...
ssh %REMOTE_USER%@%REMOTE_HOST% "mkdir -p %REMOTE_DIR%/dist"
scp -r "%DIST_DIR%\*" %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%/dist/
if errorlevel 1 (
    echo [ERROR] Upload failed!
    pause
    exit /b 1
)
echo Upload success
echo.

echo [Step 3/3] Restarting nginx...
ssh %REMOTE_USER%@%REMOTE_HOST% "brew services restart nginx"
if errorlevel 1 (
    echo [ERROR] Nginx restart failed!
    pause
    exit /b 1
)
echo Nginx restarted
echo.

echo ========================================
echo UI Deploy finished!
echo ========================================
pause
