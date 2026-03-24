@echo off
chcp 65001 >nul
echo ========================================
echo OPC Deploy Script
echo ========================================

set "JAR_FILE=opc-admin\target\opc-admin.jar"
set "REMOTE_HOST=vps-sg-aws-opc.43046721.xyz"
set "REMOTE_PORT=3011"
set "REMOTE_USER=ubuntu"
set "REMOTE_DIR=/Data/service/opc"
set "TEMP_KEY_FILE=%TEMP%\opc_key_%RANDOM%.tmp"

echo.
echo [Step 0/4] Creating temporary key file...
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
echo [Step 1/4] Cleaning and Building project...
echo ----------------------------------------
cd /d "%~dp0.."
:: Try to kill Java processes that might lock files
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM javaw.exe >nul 2>&1
:: Force delete target directories
rmdir /S /Q opc-admin\target >nul 2>&1
rmdir /S /Q opc-core\target >nul 2>&1
timeout /T 2 >nul
call mvn clean install -DskipTests
if errorlevel 1 (
    echo [ERROR] Maven build failed!
    exit /b 1
)
echo [OK] Build completed!

if not exist "%JAR_FILE%" (
    echo [ERROR] JAR file not found: %JAR_FILE%
    exit /b 1
)

echo.
echo [Step 2/4] Uploading JAR to server...
echo ----------------------------------------
echo Target: %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%
scp -P %REMOTE_PORT% -i "%TEMP_KEY_FILE%" -o StrictHostKeyChecking=no "%JAR_FILE%" "%REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%/"
if errorlevel 1 (
    echo [ERROR] Upload failed!
    exit /b 1
)
echo [OK] Upload completed!

echo.
echo [Step 3/4] Running run.sh on server...
echo ----------------------------------------
ssh -p %REMOTE_PORT% -i "%TEMP_KEY_FILE%" -o StrictHostKeyChecking=no "%REMOTE_USER%@%REMOTE_HOST%" "cd %REMOTE_DIR% && sudo nohup ./run.sh > /dev/null 2>&1 &"
if errorlevel 1 (
    echo [ERROR] Remote command failed!
    exit /b 1
)
echo [OK] Service restarted!

echo.
echo [Step 4/4] Cleaning up...
echo ----------------------------------------
del /F /Q "%TEMP_KEY_FILE%" >nul 2>&1
echo [OK] Cleanup completed!

echo.
echo ========================================
echo [DONE] Deploy success!
echo ========================================

pause
