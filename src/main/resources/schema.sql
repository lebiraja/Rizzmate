-- AI Resume Builder Database Schema
-- PostgreSQL Script

-- Create resume_data table
CREATE TABLE IF NOT EXISTS resume_data (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    location VARCHAR(100),
    profile_photo VARCHAR(255),
    career_objective TEXT,
    professional_summary TEXT,
    enhanced_career_objective TEXT,
    enhanced_professional_summary TEXT,
    enhanced_data TEXT,
    resume_score DOUBLE PRECISION,
    resume_score_feedback VARCHAR(1000),
    template VARCHAR(50) DEFAULT 'classic',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_at TIMESTAMP,
    CONSTRAINT resume_data_email_unique UNIQUE (email)
);

-- Create education table
CREATE TABLE IF NOT EXISTS education (
    id BIGSERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    degree VARCHAR(100) NOT NULL,
    university VARCHAR(200) NOT NULL,
    field_of_study VARCHAR(100) NOT NULL,
    graduation_year INTEGER NOT NULL,
    cgpa DOUBLE PRECISION,
    achievements VARCHAR(500),
    CONSTRAINT fk_education_resume FOREIGN KEY (resume_id) REFERENCES resume_data(id) ON DELETE CASCADE
);

-- Create skill table
CREATE TABLE IF NOT EXISTS skill (
    id BIGSERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    proficiency VARCHAR(50),
    description VARCHAR(500),
    CONSTRAINT fk_skill_resume FOREIGN KEY (resume_id) REFERENCES resume_data(id) ON DELETE CASCADE
);

-- Create project table
CREATE TABLE IF NOT EXISTS project (
    id BIGSERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    project_name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    technologies VARCHAR(500),
    project_link VARCHAR(255),
    achievements VARCHAR(500),
    start_date VARCHAR(50),
    end_date VARCHAR(50),
    CONSTRAINT fk_project_resume FOREIGN KEY (resume_id) REFERENCES resume_data(id) ON DELETE CASCADE
);

-- Create certification table
CREATE TABLE IF NOT EXISTS certification (
    id BIGSERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    certification_name VARCHAR(200) NOT NULL,
    issuer VARCHAR(200),
    issue_date VARCHAR(50),
    expiry_date VARCHAR(50),
    description VARCHAR(500),
    certificate_link VARCHAR(255),
    CONSTRAINT fk_certification_resume FOREIGN KEY (resume_id) REFERENCES resume_data(id) ON DELETE CASCADE
);

-- Create language table
CREATE TABLE IF NOT EXISTS language (
    id BIGSERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    language_name VARCHAR(50) NOT NULL,
    proficiency VARCHAR(50),
    CONSTRAINT fk_language_resume FOREIGN KEY (resume_id) REFERENCES resume_data(id) ON DELETE CASCADE
);

-- Create achievement table
CREATE TABLE IF NOT EXISTS achievement (
    id BIGSERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    achievement_title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    date VARCHAR(50),
    category VARCHAR(100),
    CONSTRAINT fk_achievement_resume FOREIGN KEY (resume_id) REFERENCES resume_data(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_resume_email ON resume_data(email);
CREATE INDEX idx_resume_created_at ON resume_data(created_at);
CREATE INDEX idx_education_resume_id ON education(resume_id);
CREATE INDEX idx_skill_resume_id ON skill(resume_id);
CREATE INDEX idx_project_resume_id ON project(resume_id);
CREATE INDEX idx_certification_resume_id ON certification(resume_id);
CREATE INDEX idx_language_resume_id ON language(resume_id);
CREATE INDEX idx_achievement_resume_id ON achievement(resume_id);
