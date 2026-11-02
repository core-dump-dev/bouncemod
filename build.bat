@echo off
chcp 65001 >nul
setlocal

cd /d "%~dp0"

echo ========================================
echo   Сборка Bounce Mod
echo ========================================
echo.

echo [1/4] Остановка Gradle daemon...
call gradlew.bat --stop >nul 2>&1

echo [2/4] Очистка кэша...
if exist ".gradle" rmdir /s /q ".gradle"
if exist "build"   rmdir /s /q "build"

echo [3/4] Сборка...
call gradlew.bat build --no-configuration-cache --console plain
if errorlevel 1 (
    echo.
    echo ========================================
    echo   ОШИБКА СБОРКИ
    echo ========================================
    pause
    exit /b 1
)

echo.
echo ========================================
echo   ГОТОВО
echo ========================================
echo.
echo JAR-файл:
dir /b build\libs\*.jar | findstr /v "sources"
echo.
echo Полный путь:
echo   %cd%\build\libs\
echo.

pause
endlocal