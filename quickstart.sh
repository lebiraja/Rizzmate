#!/bin/bash
# AI Resume Builder - Quick Start Script for Linux/macOS

echo "🚀 AI Resume Builder - Quick Start"
echo "===================================="

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker not found. Please install Docker Desktop."
    exit 1
fi

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose not found. Please install Docker Desktop."
    exit 1
fi

# Check if .env file exists
if [ ! -f .env ]; then
    echo "📝 Creating .env file from .env.example..."
    cp .env.example .env
    echo "⚠️  Please edit .env and add your GEMINI_API_KEY"
    echo "📍 Visit: https://aistudio.google.com/ to get your API key"
    exit 1
fi

# Check if Gemini API key is set
if grep -q "GEMINI_API_KEY=your_gemini_api_key_here" .env; then
    echo "❌ Please set GEMINI_API_KEY in .env file"
    echo "📍 Visit: https://aistudio.google.com/ to get your API key"
    exit 1
fi

echo "✅ Prerequisites checked"
echo ""

# Ask user if they want to build
read -p "Build and start the application? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🔨 Building and starting services..."
    docker-compose up --build
else
    echo "👋 Exiting..."
    exit 0
fi
