@echo off
REM AI Resume Builder - Quick Start Script for Windows

echo.
echo ========================================
echo    AI Resume Builder - Quick Start
echo ========================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker not found. Please install Docker Desktop.
    echo 📍 Download: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Check if docker-compose is installed
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker Compose not found. Please install Docker Desktop.
    pause
    exit /b 1
)

echo ✅ Docker installed
echo.

REM Check if .env file exists
if not exist .env (
    echo 📝 Creating .env file from .env.example...
    copy .env.example .env
    echo.
    echo ⚠️  Please edit .env and add your GEMINI_API_KEY
    echo 📍 Visit: https://aistudio.google.com/ to get your API key
    echo.
    pause
    exit /b 1
)

REM Check if Gemini API key is set
findstr /M "your_gemini_api_key_here" .env >nul 2>&1
if %errorlevel% equ 0 (
    echo ❌ Please set GEMINI_API_KEY in .env file
    echo 📍 Visit: https://aistudio.google.com/ to get your API key
    echo.
    pause
    exit /b 1
)

echo ✅ Prerequisites checked
echo.

REM Display next steps
echo 🚀 Starting AI Resume Builder...
echo.
echo The following services will start:
echo   - PostgreSQL Database on port 5432
echo   - Spring Boot Application on port 8080
echo.
echo 📍 Access the application at: http://localhost:8080
echo.

REM Start docker-compose
docker-compose up --build

pause
