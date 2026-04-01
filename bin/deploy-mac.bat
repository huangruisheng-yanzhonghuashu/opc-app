@echo off
chcp 65001 >nul
echo ========================================
echo OPC Admin Deploy Script
echo Target: 192.168.109 (SSH Key Auth)
echo ========================================
echo.

set REMOTE_HOST=192.168.109
set REMOTE_USER=sevilinma
set REMOTE_DIR=/Users/sevilinma/opc

REM Get script directory and find project root
set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..
set LOCAL_JAR=%PROJECT_ROOT%\opc-admin\target\opc-admin.jar

echo Project root: %PROJECT_ROOT%
echo JAR path: %LOCAL_JAR%
echo.

REM Step 1: Maven Build
echo [Step 1/6] Building with Maven...
cd /d %PROJECT_ROOT%
call mvn clean package -DskipTests -pl opc-admin -am
if %errorlevel% neq 0 (
    echo [ERROR] Maven build failed!
    pause
    exit /b 1
)
echo Build success
echo.

if not exist %LOCAL_JAR% (
    echo [ERROR] JAR not found: %LOCAL_JAR%
    pause
    exit /b 1
)

echo [Step 2/6] Checking JAR file...
for %%I in (%LOCAL_JAR%) do echo Size: %%~zI bytes
echo.

echo [Step 3/6] Uploading JAR to server...
scp %LOCAL_JAR% %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_DIR%/opc-admin.jar.new
if %errorlevel% neq 0 (
    echo [ERROR] Upload failed!
    echo Please run setup-ssh-key.bat first to configure SSH key.
    pause
    exit /b 1
)
echo Upload success
echo.

echo [Step 4/6] Stopping old service (port 8080)...
ssh %REMOTE_USER%@%REMOTE_HOST% "lsof -ti:8080 | xargs kill -9 2>/dev/null; echo 'Port 8080 cleared'"
echo.

echo [Step 5/6] Deploying new JAR and starting service...
ssh %REMOTE_USER%@%REMOTE_HOST% "cd %REMOTE_DIR% && ls opc-admin.jar 2>/dev/null && mv opc-admin.jar opc-admin.jar.backup.$(date +%%Y%%m%%d_%%H%%M%%S); echo 'Backup done'"
ssh %REMOTE_USER%@%REMOTE_HOST% "cd %REMOTE_DIR% && mv opc-admin.jar.new opc-admin.jar"
ssh %REMOTE_USER%@%REMOTE_HOST% "cd %REMOTE_DIR% && nohup java -jar opc-admin.jar > app.log 2>&1 &"
echo Service started
echo.

echo [Step 6/6] Checking service status...
timeout /t 3 /nobreak >nul
ssh %REMOTE_USER%@%REMOTE_HOST% "ps aux | grep 'java -jar opc-admin.jar' | grep -v grep"
ssh %REMOTE_USER%@%REMOTE_HOST% "lsof -i:8080 2>/dev/null | head -3"
echo.

echo ========================================
echo Deploy finished!
echo ========================================
pause
