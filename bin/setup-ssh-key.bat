@echo off
chcp 65001 >nul
echo ========================================
echo SSH Key Setup for OPC Deploy
echo ========================================
echo.

set REMOTE_HOST=192.168.109
set REMOTE_USER=sevilinma
set KEY_FILE=%USERPROFILE%\.ssh\id_rsa

echo [Step 1/3] Checking SSH key...
if not exist "%KEY_FILE%" (
    echo SSH key not found. Generating new key pair...
    ssh-keygen -t rsa -b 4096 -f "%KEY_FILE%" -N "" -C "deploy-key"
    echo Key generated
) else (
    echo SSH key exists
)
echo.

echo [Step 2/3] Copying public key to server...
echo Enter password: 7895123
echo.

scp "%KEY_FILE%.pub" %REMOTE_USER%@%REMOTE_HOST%:/tmp/key.pub
if errorlevel 1 goto error

ssh %REMOTE_USER%@%REMOTE_HOST% "mkdir -p ~/.ssh && cat /tmp/key.pub >> ~/.ssh/authorized_keys && chmod 700 ~/.ssh && chmod 600 ~/.ssh/authorized_keys && rm /tmp/key.pub"
if errorlevel 1 goto error

echo.
echo [Step 3/3] Testing connection...
ssh -o PasswordAuthentication=no %REMOTE_USER%@%REMOTE_HOST% "echo Success"
if errorlevel 1 goto error

echo.
echo ========================================
echo SSH key setup completed!
echo ========================================
pause
exit /b 0

:error
echo [ERROR] Setup failed!
pause
exit /b 1
