package com.airesumebuilder.service;

import com.airesumebuilder.config.GeminiConfig;
import com.airesumebuilder.dto.EnhancementRequestDTO;
import com.airesumebuilder.exception.GeminiApiException;
import com.airesumebuilder.exception.RateLimitExceededException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Service class for Gemini API integration.
 * Handles all AI-powered text enhancement and resume scoring operations.
 *
 * Features:
 * - Enhance resume content using Gemini API
 * - Generate resume improvement suggestions
 * - Calculate resume score based on content quality
 * - Rate limiting to prevent API quota exhaustion
 * - Retry logic with exponential backoff
 */
@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    @Autowired
    private GeminiConfig geminiConfig;

    @Autowired
    private RestTemplate restTemplate;

    private final Gson gson = new Gson();
    private final Bucket rateLimiter;

    public GeminiService() {
        // Rate limit: 10 requests per minute
        Bandwidth limit = Bandwidth.classic(MAX_REQUESTS_PER_MINUTE, 
            Refill.intervally(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        this.rateLimiter = Bucket.builder()
            .addLimit(limit)
            .build();
    }

    /**
     * Enhance resume content using Gemini API
     * Sends text to Gemini and receives professionally improved content
     */
    @Retryable(
        retryFor = {RestClientException.class, HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String enhanceResumeContent(EnhancementRequestDTO request) {
        String prompt = buildEnhancementPrompt(request);
        String response = callGeminiAPI(prompt);
        log.info("Successfully enhanced resume content");
        return response;
    }

    /**
     * Recover method for enhancement failures
     */
    @Recover
    public String recoverEnhancement(Exception e, EnhancementRequestDTO request) {
        log.error("Failed to enhance resume after 3 attempts", e);
        throw new GeminiApiException("AI service unavailable after retries", e);
    }

    /**
     * Calculate resume score using Gemini API
     * Analyzes resume quality and provides improvement suggestions
     */
    @Retryable(
        retryFor = {RestClientException.class, HttpServerErrorException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String calculateResumeScore(EnhancementRequestDTO request) {
        String prompt = buildScoringPrompt(request);
        String response = callGeminiAPI(prompt);
        log.info("Successfully calculated resume score");
        return response;
    }

    /**
     * Recover method for scoring failures
     */
    @Recover
    public String recoverScoring(Exception e, EnhancementRequestDTO request) {
        log.error("Failed to calculate resume score after 3 attempts", e);
        throw new GeminiApiException("AI service unavailable after retries", e);
    }

    /**
     * Call Gemini API with the given prompt
     */
    private String callGeminiAPI(String prompt) {
        // Check rate limit
        if (!rateLimiter.tryConsume(1)) {
            throw new RateLimitExceededException(
                "Gemini API rate limit exceeded. Maximum " + MAX_REQUESTS_PER_MINUTE + " requests per minute.");
        }

        try {
            String url = geminiConfig.getEndpoint() + "?key=" + geminiConfig.getKey();

            // Build request body
            JsonObject requestBody = new JsonObject();
            
            JsonArray contents = new JsonArray();
            JsonObject content = new JsonObject();
            
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", prompt);
            parts.add(part);
            
            content.add("parts", parts);
            contents.add(content);
            
            requestBody.add("contents", contents);

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Make request
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Parse response and extract text
                return extractTextFromGeminiResponse(response.getBody());
            } else {
                throw new GeminiApiException("Gemini API returned error: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("Gemini API client error: {}", e.getMessage(), e);
            throw new GeminiApiException("Invalid request to AI service", e);
        } catch (HttpServerErrorException e) {
            log.error("Gemini API server error: {}", e.getMessage(), e);
            throw new GeminiApiException("AI service temporarily unavailable", e);
        } catch (RestClientException e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to communicate with AI service", e);
        }
    }

    /**
     * Extract text content from Gemini API response
     */
    private String extractTextFromGeminiResponse(String responseBody) {
        try {
            JsonObject response = gson.fromJson(responseBody, JsonObject.class);
            
            if (response.has("candidates") && response.getAsJsonArray("candidates").size() > 0) {
                JsonObject candidate = response.getAsJsonArray("candidates").get(0).getAsJsonObject();
                
                if (candidate.has("content") && candidate.getAsJsonObject("content").has("parts")) {
                    JsonArray parts = candidate.getAsJsonObject("content").getAsJsonArray("parts");
                    
                    if (parts.size() > 0) {
                        JsonObject part = parts.get(0).getAsJsonObject();
                        return part.get("text").getAsString();
                    }
                }
            }
            
            throw new GeminiApiException("Unexpected Gemini API response format");
        } catch (JsonSyntaxException e) {
            log.error("Error parsing Gemini response: {}", e.getMessage(), e);
            throw new GeminiApiException("Invalid response format from AI service", e);
        }
    }

    /**
     * Build enhancement prompt for Gemini API
     */
    private String buildEnhancementPrompt(EnhancementRequestDTO request) {
        return """
            You are a professional resume writing expert specializing in helping fresh graduates and college students.
            
            Your task is to rewrite and improve the following resume content to sound more professional, concise,
            and impactful. Make the content more compelling while keeping the meaning intact.
            
            Focus on:
            - Using strong action verbs
            - Quantifying achievements where possible
            - Making statements more concise and impactful
            - Ensuring proper grammar and professional tone
            - Highlighting relevant skills and experiences
            
            Resume Content:
            %s
            
            Provide the enhanced content in the following JSON format:
            {
              "enhancedCareerObjective": "An improved, professional career objective statement",
              "enhancedProfessionalSummary": "An improved professional summary highlighting key strengths",
              "enhancedSkills": "Professionally phrased skills section",
              "enhancedProjects": "Improved project descriptions with impact and results",
              "enhancedAchievements": "Improved achievements with quantified results"
            }
            
            IMPORTANT: Return ONLY valid JSON, no additional text before or after the JSON object.
            """.formatted(request.getCombinedText());
    }

    /**
     * Build scoring prompt for Gemini API
     */
    private String buildScoringPrompt(EnhancementRequestDTO request) {
        return """
            You are a resume evaluation expert with experience in recruiting for tech companies.
            
            Analyze the following resume content for a fresh graduate/college student and provide a comprehensive evaluation.
            
            Consider these criteria:
            - Content quality and relevance (30 points)
            - Professional presentation and formatting (20 points)
            - Skill demonstration and technical knowledge (25 points)
            - Project descriptions and impact (15 points)
            - Overall marketability for entry-level positions (10 points)
            
            Resume Content:
            %s
            
            Provide the evaluation in the following JSON format:
            {
              "score": 85,
              "feedback": "Overall assessment and key points",
              "strengths": ["Strength 1", "Strength 2", "Strength 3"],
              "improvements": ["Area to improve 1", "Area to improve 2", "Area to improve 3"],
              "actionItems": ["Specific action 1", "Specific action 2", "Specific action 3"]
            }
            
            IMPORTANT: Return ONLY valid JSON, no additional text before or after the JSON object.
            """.formatted(request.getCombinedText());
    }
}
