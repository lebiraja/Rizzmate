package com.airesumebuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for AI Resume Builder Spring Boot application.
 * This application helps college freshers create professional resumes
 * by using AI enhancement and PDF generation capabilities.
 *
 * Features:
 * - User resume data collection via web forms
 * - AI-powered text enhancement using Gemini API
 * - Professional PDF resume generation
 * - PostgreSQL database persistence
 *
 * @author AI Resume Builder Team
 * @version 1.0.0
 */
@SpringBootApplication
public class AiResumeBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiResumeBuilderApplication.class, args);
    }
}
