@echo off
chcp 65001 >nul
echo ========================================
echo OPC Admin UI Deploy Script
echo ========================================

set "DIST_DIR=dist"
set "REMOTE_HOST=vps-sg-aws-opc.43046721.xyz"
set "REMOTE_PORT=3011"
set "REMOTE_USER=ubuntu"
set "REMOTE_DIR=/Data/service/ROOT/web/www"
set "TEMP_KEY_FILE=%TEMP%\opc_key_%RANDOM%.tmp"

echo.
echo [Step 0/3] Creating temporary key file...
echo ----------------------------------------
(
echo -----BEGIN OPENSSH PRIVATE KEY-----
echo b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW
echo QyNTUxOQAAACBwZIGaqfZielhtehgCCEgjQ4xMlBDelECdyEeMN1U2IQAAAJiOx6Ycjsem
echo HAAAAAtzc2gtZWQyNTUxOQAAACBwZIGaqfZielhtehgCCEgjQ4xMlBDelECdyEeMN1U2IQ
echo AAAEBvX6wmHEi3NzirnI+sVwqjDUcVStv7XUPl/Z54URConXBkgZqp9mJ6WG16GAIISCND
echo jEyUEN6UQJ3IR4w3VTYhAAAAE215LWxpbnV4LXNlcnZlci1rZXkBAg==
echo -----END OPENSSH PRIVATE KEY-----
) > "%TEMP_KEY_FILE%"
icacls "%TEMP_KEY_FILE%" /inheritance:r >nul 2>&1
icacls "%TEMP_KEY_FILE%" /remove "NT AUTHORITY\Authenticated Users" >nul 2>&1
icacls "%TEMP_KEY_FILE%" /remove "BUILTIN\Users" >nul 2>&1
icacls "%TEMP_KEY_FILE%" /grant "%USERNAME%:RX" >nul 2>&1
echo [OK] Temp key file created!

echo.
echo [Step 1/3] Building UI project...
echo ----------------------------------------
cd /d "%~dp0..\opc-admin-ui"
call yarn build:stage
if errorlevel 1 (
    echo [ERROR] Build failed!
    exit /b 1
)
echo [OK] Build completed!

if not exist "%DIST_DIR%" (
    echo [ERROR] Dist folder not found: %DIST_DIR%
    exit /b 1
)

echo.
echo [Step 2/3] Uploading files to server...
echo ----------------------------------------
echo Target: %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%
scp -P %REMOTE_PORT% -i "%TEMP_KEY_FILE%" -o StrictHostKeyChecking=no -r "%DIST_DIR%\*" "%REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%/"
if errorlevel 1 (
    echo [ERROR] Upload failed!
    exit /b 1
)
echo [OK] Upload completed!

echo.
echo [Step 3/3] Cleaning up...
echo ----------------------------------------
del /F /Q "%TEMP_KEY_FILE%" >nul 2>&1
echo [OK] Cleanup completed!

echo.
echo ========================================
echo [DONE] UI Deploy success!
echo ========================================

pause
