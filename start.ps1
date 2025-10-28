# AI Resume Builder - PowerShell Startup Script
# Loads environment variables from .env file and starts the Spring Boot application

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  AI Resume Builder - Startup  " -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Check if .env file exists
if (Test-Path ".env") {
    Write-Host "Loading environment variables from .env file..." -ForegroundColor Green
    
    # Read .env file and set environment variables
    Get-Content .env | ForEach-Object {
        if ($_ -match '^([^#][^=]+)=(.*)$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
            Write-Host "  ✓ Set $name" -ForegroundColor Gray
        }
    }
    Write-Host ""
} else {
    Write-Host "WARNING: .env file not found!" -ForegroundColor Yellow
    Write-Host "Please create .env file from .env.example" -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

# Check if PostgreSQL is running
Write-Host "Checking PostgreSQL service..." -ForegroundColor Green
$postgresService = Get-Service -Name "*postgre*" -ErrorAction SilentlyContinue
if ($postgresService -and $postgresService.Status -eq "Running") {
    Write-Host "  ✓ PostgreSQL is running" -ForegroundColor Gray
} else {
    Write-Host "  ✗ PostgreSQL is not running. Please start PostgreSQL first." -ForegroundColor Red
    exit 1
}
Write-Host ""

# Start the application
Write-Host "Starting AI Resume Builder..." -ForegroundColor Green
Write-Host "  → Server will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "  → Press Ctrl+C to stop the server" -ForegroundColor Gray
Write-Host ""

mvn spring-boot:run
