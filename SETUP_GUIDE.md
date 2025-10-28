# AI Resume Builder - Complete Setup Guide

## 📌 Quick Start (5 minutes)

### Option 1: Using Docker Compose (Easiest)

```bash
# 1. Clone the project
git clone <repository-url>
cd ai-resume-builder

# 2. Get your Gemini API Key
# Visit: https://aistudio.google.com/
# Create API Key and copy it

# 3. Set environment variable
export GEMINI_API_KEY=your_key_here

# 4. Start everything
docker-compose up --build

# 5. Open browser
# http://localhost:8080
```

### Option 2: Local Development (10 minutes)

#### Step 1: Install Prerequisites
- Java 17+: https://www.oracle.com/java/technologies/downloads/
- PostgreSQL: https://www.postgresql.org/download/
- Maven: https://maven.apache.org/download.cgi

#### Step 2: Create Database
```bash
# Windows (PowerShell)
psql -U postgres -c "CREATE DATABASE ai_resume_builder;"

# macOS/Linux
createdb ai_resume_builder
```

#### Step 3: Get Gemini API Key
1. Go to https://aistudio.google.com/
2. Sign in with Google account
3. Click "Create API Key"
4. Copy the generated key

#### Step 4: Configure Application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_resume_builder
spring.datasource.username=postgres
spring.datasource.password=postgres
gemini.api.key=YOUR_KEY_HERE
```

#### Step 5: Build and Run
```bash
# Build project
mvn clean package -DskipTests

# Run Spring Boot
mvn spring-boot:run

# Or run JAR directly
java -jar target/ai-resume-builder-1.0.0.jar
```

#### Step 6: Access Application
```
http://localhost:8080
```

---

## 🔧 Detailed Setup Instructions

### Windows Setup

#### 1. Install Java 17

```powershell
# Download from Oracle or use Chocolatey
choco install openjdk17

# Verify installation
java -version
```

#### 2. Install PostgreSQL

```powershell
# Using Chocolatey
choco install postgresql

# Or download from: https://www.postgresql.org/download/windows/
# Follow the installer wizard
```

#### 3. Install Maven

```powershell
# Using Chocolatey
choco install maven

# Verify
mvn --version
```

#### 4. Create Database

```powershell
# Open PostgreSQL cmd
psql -U postgres

# In PostgreSQL shell
CREATE DATABASE ai_resume_builder;
\q
```

#### 5. Clone Project

```powershell
git clone <repository-url>
cd ai-resume-builder
```

#### 6. Configure Properties

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_resume_builder
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password
gemini.api.key=YOUR_GEMINI_KEY
```

#### 7. Build and Run

```powershell
mvn clean install -DskipTests
mvn spring-boot:run
```

### macOS Setup

#### 1. Install Java 17

```bash
brew install openjdk@17
```

#### 2. Install PostgreSQL

```bash
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15
```

#### 3. Install Maven

```bash
brew install maven
```

#### 4. Create Database

```bash
createdb ai_resume_builder
```

#### 5. Rest is same as Windows

### Linux (Ubuntu) Setup

#### 1. Install Java 17

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

#### 2. Install PostgreSQL

```bash
sudo apt install postgresql postgresql-contrib

# Start service
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

#### 3. Install Maven

```bash
sudo apt install maven
```

#### 4. Create Database

```bash
sudo -u postgres createdb ai_resume_builder
```

#### 5. Rest is same as Windows

---

## 🐳 Docker Setup

### Prerequisites
- Docker Desktop: https://www.docker.com/products/docker-desktop
- Docker Compose (included with Docker Desktop)

### Method 1: Docker Compose (Recommended)

```bash
# Create .env file
cp .env.example .env

# Edit .env and add Gemini API Key
nano .env
# Change: GEMINI_API_KEY=your_key_here

# Start services
docker-compose up --build

# Access application
# http://localhost:8080

# To stop
docker-compose down

# To view logs
docker-compose logs -f app
```

### Method 2: Manual Docker Commands

```bash
# Build Maven project first
mvn clean package -DskipTests

# Start PostgreSQL
docker run -d \
  --name postgres-ai \
  -e POSTGRES_DB=ai_resume_builder \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine

# Build application image
docker build -t ai-resume-builder:1.0 .

# Run application
docker run -d \
  --name ai-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-ai:5432/ai_resume_builder \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e GEMINI_API_KEY=your_key_here \
  --link postgres-ai:postgres-ai \
  ai-resume-builder:1.0

# Check status
docker ps

# View logs
docker logs -f ai-app

# Stop containers
docker stop ai-app postgres-ai
docker rm ai-app postgres-ai
```

---

## ✅ Verification Checklist

After setup, verify everything works:

- [ ] Application starts without errors
- [ ] Can access http://localhost:8080 in browser
- [ ] Home page loads with all features visible
- [ ] Can fill resume form without validation errors
- [ ] Can add education, skills, projects, etc.
- [ ] Template selection works
- [ ] "Enhance with AI" button works (needs valid Gemini API key)
- [ ] "Generate Resume" creates PDF download

---

## 🔐 Getting Gemini API Key

### Step-by-Step

1. **Visit Google AI Studio**
   - Go to: https://aistudio.google.com/

2. **Sign In**
   - Use your Google account
   - Create account if needed

3. **Create API Key**
   - Click "Get API Key" (top left)
   - Click "Create API key in new project"
   - Click "Create API key"

4. **Copy Key**
   - Copy the displayed API key
   - Keep it safe (don't commit to Git!)

5. **Add to Configuration**
   - Paste in `application.properties`: `gemini.api.key=YOUR_KEY`
   - Or set environment variable: `GEMINI_API_KEY=YOUR_KEY`

### Enable Required APIs

The API key automatically enables:
- Generative Language API
- All necessary Gemini models

---

## 🧪 Testing the Application

### Manual Testing

1. **Open Home Page**
   ```
   http://localhost:8080/
   ```
   - Verify all sections load
   - Click "Start Building Your Resume"

2. **Fill Resume Form**
   - Enter personal info
   - Add education
   - Add skills
   - Add projects

3. **Test AI Enhancement**
   - Click "Enhance with AI"
   - Verify response appears

4. **Generate Resume**
   - Click "Generate Resume"
   - PDF should download
   - Check PDF quality

### API Testing with cURL

```bash
# Create Resume
curl -X POST http://localhost:8080/api/resume/submit \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "location": "San Francisco",
    "careerObjective": "Seeking software developer role"
  }'

# Get Resume (replace {id} with actual ID)
curl http://localhost:8080/api/resume/1

# Download PDF
curl -X GET http://localhost:8080/api/resume/1/pdf > resume.pdf
```

---

## 🐛 Troubleshooting

### PostgreSQL Connection Error

**Error**: `PSQLException: Connection to localhost:5432 refused`

**Solutions**:
```bash
# Check if PostgreSQL is running
# Windows
pg_isready

# macOS/Linux
pg_isready -h localhost

# If not running, start it
# Windows
net start PostgreSQL-x64-15

# macOS
brew services start postgresql@15

# Linux
sudo systemctl start postgresql
```

### Gemini API Error

**Error**: `API key is invalid`

**Solutions**:
- Verify API key is correct from aistudio.google.com
- Check for extra spaces in API key
- Ensure API key has not expired
- Check API quota limits

### Port Already in Use

**Error**: `Address already in use: bind`

**Solutions**:
```bash
# Change port in application.properties
server.port=8081

# Or kill process using port 8080
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -i :8080
kill -9 <PID>
```

### PDF Generation Fails

**Error**: PDF generation throws exception

**Solutions**:
- Ensure all form fields are filled
- Check Java version is 17+
- Check console logs for detailed error
- Try with minimal data first

---

## 🚀 Deployment

### Heroku Deployment

```bash
# Create Heroku app
heroku create ai-resume-builder

# Add PostgreSQL addon
heroku addons:create heroku-postgresql:mini

# Set Gemini API key
heroku config:set GEMINI_API_KEY=your_key_here

# Deploy from Git
git push heroku main

# Check logs
heroku logs --tail
```

### AWS Elastic Beanstalk

```bash
# Install EB CLI
pip install awsebcli

# Initialize
eb init -p "Java 17 running on 64bit Amazon Linux 2"

# Create environment
eb create ai-resume-builder-env

# Set environment variables
eb setenv GEMINI_API_KEY=your_key_here

# Deploy
eb deploy

# Open application
eb open
```

### Railway.app

1. Push code to GitHub
2. Connect Railway to GitHub
3. Add PostgreSQL service
4. Set environment variables
5. Deploy

---

## 📚 Project Structure Reference

```
ai-resume-builder/
├── src/main/java/com/airesumebuilder/
│   ├── controller/           # HTTP endpoints
│   ├── service/              # Business logic
│   ├── model/                # Database entities
│   ├── repository/           # Data access
│   ├── dto/                  # Data transfer objects
│   ├── config/               # Configuration classes
│   └── AiResumeBuilderApplication.java
├── src/main/resources/
│   ├── templates/            # HTML pages
│   ├── application.properties
│   └── schema.sql
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── .env.example
└── README.md
```

---

## 💡 Next Steps After Setup

1. **Customize Templates**
   - Edit CSS in `PdfService.java`

2. **Modify AI Prompts**
   - Update prompts in `GeminiService.java`

3. **Add New Features**
   - Follow existing architecture patterns
   - Add new entity → DTO → Repository → Service → Controller

4. **Deploy to Cloud**
   - Use Docker Compose on Cloud VMs
   - Or use platform-specific deployment (Heroku, Railway, etc.)

---

## 📞 Support

If you encounter issues:
1. Check Troubleshooting section above
2. Review application logs: `tail -f application.log`
3. Check database connection
4. Verify Gemini API key
5. Open GitHub issue with error details

---

**You're all set! Happy building! 🚀**
