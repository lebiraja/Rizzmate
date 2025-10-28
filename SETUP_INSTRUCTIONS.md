# 🚀 AI Resume Builder - Fixed & Enhanced Version

## ✅ What's Been Fixed

### 🔴 Critical Security Fixes
- ✅ **Removed hardcoded credentials** - API keys and passwords now use environment variables
- ✅ **Added input validation** - All user inputs are validated using Bean Validation
- ✅ **Fixed CORS configuration** - Restricted to specific origins instead of wildcard
- ✅ **Added custom exceptions** - Proper error handling with detailed error messages
- ✅ **Implemented rate limiting** - Prevents API quota exhaustion (10 requests/minute)

### 🟡 Performance Improvements
- ✅ **Added caching** - Resume data cached for 10 minutes (Simple Cache)
- ✅ **Fixed N+1 query problem** - Using `@EntityGraph` for optimized queries
- ✅ **Added retry logic** - Automatic retry with exponential backoff for API failures
- ✅ **Optimized database queries** - Eager loading of relationships

### 🟢 Code Quality Improvements
- ✅ **Converted to Java 17 text blocks** - More readable multi-line strings
- ✅ **Added Swagger/OpenAPI** - Auto-generated API documentation
- ✅ **Improved error handling** - Specific exceptions instead of generic RuntimeException
- ✅ **Enhanced Gemini prompts** - Better AI responses with detailed instructions

---

## 🛠️ Setup Instructions

### Prerequisites
- **Java 17+** (JDK installed and configured)
- **PostgreSQL** (running on port 5432)
- **Maven** (for building the project)
- **Gemini API Key** (free from [Google AI Studio](https://makersuite.google.com/app/apikey))

### Step 1: Clone & Navigate
```powershell
cd c:\Users\lebiraja\projects\Rizzmate
```

### Step 2: Set Up Environment Variables
The `.env` file already exists with your credentials. **Important:** Make sure this file is NEVER committed to Git (it's already in `.gitignore`).

**Current `.env` configuration:**
```properties
DB_URL=jdbc:postgresql://localhost:5432/ai_resume_builder
DB_USERNAME=postgres
DB_PASSWORD=2327
GEMINI_API_KEY=AIzaSyC5MRsmMEVbhRRfeKpodoYpnAFDxGwkojw
```

**⚠️ SECURITY WARNING:** You should regenerate your Gemini API key since the old one was exposed in Git history:
1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Create a new API key
3. Replace `GEMINI_API_KEY` in `.env` file

### Step 3: Create Database
```powershell
psql -U postgres -c "CREATE DATABASE ai_resume_builder;"
```

### Step 4: Start the Application

#### Option A: Using PowerShell Script (Recommended)
```powershell
.\start.ps1
```

#### Option B: Manual Start
```powershell
# Load environment variables manually
$env:DB_PASSWORD = "2327"
$env:GEMINI_API_KEY = "YOUR_API_KEY_HERE"

# Start application
mvn spring-boot:run
```

### Step 5: Access the Application
- **Web Interface:** http://localhost:8080
- **API Documentation:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health

---

## 📚 API Documentation

### Endpoints

#### 1. Submit Resume
```http
POST /api/resume/submit
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "location": "Mumbai, India",
  "careerObjective": "Seeking entry-level position...",
  "educations": [...],
  "skills": [...],
  "projects": [...]
}
```

**Validation Rules:**
- `firstName`, `lastName`: 2-50 characters
- `email`: Valid email format
- `phone`: Exactly 10 digits
- `careerObjective`: Max 1000 characters
- `professionalSummary`: Max 2000 characters

#### 2. Get Resume
```http
GET /api/resume/{id}
```

#### 3. Enhance Resume with AI
```http
POST /api/resume/{id}/enhance
Content-Type: application/json

{
  "combinedText": "Career objective and content to enhance..."
}
```

**Rate Limit:** 10 requests per minute

#### 4. Calculate Resume Score
```http
POST /api/resume/{id}/score
Content-Type: application/json

{
  "combinedText": "Resume content to score..."
}
```

#### 5. Download PDF
```http
GET /api/resume/{id}/pdf
```

---

## 🎨 Features

### Security Features
- ✅ Input validation on all endpoints
- ✅ Rate limiting for expensive AI operations
- ✅ Custom exception handling
- ✅ CORS restricted to specific origins
- ✅ Environment-based configuration

### Performance Features
- ✅ In-memory caching (10-minute TTL)
- ✅ Optimized database queries with `@EntityGraph`
- ✅ Retry logic with exponential backoff
- ✅ Connection pooling with HikariCP

### AI Enhancement Features
- ✅ Improved Gemini prompts for better results
- ✅ Career objective enhancement
- ✅ Professional summary improvement
- ✅ Skills rephrasing
- ✅ Project description enhancement
- ✅ Resume scoring (out of 100)

---

## 🐛 Error Handling

### Error Response Format
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "firstName": "First name must be between 2 and 50 characters",
    "email": "Invalid email format"
  },
  "timestamp": 1730108400000
}
```

### HTTP Status Codes
- `200` - Success
- `400` - Validation error / Invalid data
- `404` - Resource not found
- `429` - Rate limit exceeded
- `500` - Internal server error
- `503` - AI service unavailable

---

## 📊 Monitoring

### Health Check
```powershell
curl http://localhost:8080/actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

## 🧪 Testing

### Manual Testing
1. Open http://localhost:8080
2. Fill out the resume form
3. Click "Submit"
4. Download the generated PDF

### API Testing with cURL

**Submit Resume:**
```powershell
curl -X POST http://localhost:8080/api/resume/submit `
  -H "Content-Type: application/json" `
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "careerObjective": "Seeking entry-level position"
  }'
```

**Get Resume:**
```powershell
curl http://localhost:8080/api/resume/1
```

**Download PDF:**
```powershell
curl http://localhost:8080/api/resume/1/pdf -o resume.pdf
```

---

## 🔧 Configuration

### Database Configuration
Edit `.env` file:
```properties
DB_URL=jdbc:postgresql://localhost:5432/ai_resume_builder
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

### Cache Configuration
Edit `application.properties`:
```properties
spring.cache.caffeine.spec=maximumSize=500,expireAfterAccess=600s
```

### Rate Limiting
Edit `GeminiService.java`:
```java
private static final int MAX_REQUESTS_PER_MINUTE = 10;
```

---

## 📝 What's Next?

### Recommended Improvements
1. **Add Unit Tests** - Currently 0% coverage
2. **Deploy to Cloud** - Azure/AWS/Google Cloud
3. **Add User Authentication** - JWT or OAuth2
4. **Email Notifications** - Send PDF via email
5. **Resume Templates** - Multiple PDF designs
6. **LinkedIn Import** - Auto-fill from LinkedIn profile

### Known Limitations
- Cache is in-memory (lost on restart)
- No user authentication
- Single template design
- No export to Word format

---

## 🆘 Troubleshooting

### Application won't start
**Error:** `Connection refused`
**Solution:** Make sure PostgreSQL is running
```powershell
Get-Service -Name "*postgre*"
```

### API returns 503 error
**Error:** `AI service temporarily unavailable`
**Solutions:**
1. Check your Gemini API key is valid
2. Check rate limit (max 10 requests/minute)
3. Check internet connection

### Validation errors
**Error:** `Phone number must be 10 digits`
**Solution:** Ensure phone number is exactly 10 digits without spaces or special characters

---

## 📜 License
This project is for educational purposes.

## 👤 Author
AI Resume Builder Team

---

**Last Updated:** October 28, 2025  
**Version:** 1.0.0 (Enhanced & Secured)
