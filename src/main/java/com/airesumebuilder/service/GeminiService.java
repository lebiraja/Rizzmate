package com.airesumebuilder.service;

import com.airesumebuilder.config.GeminiConfig;
import com.airesumebuilder.dto.EnhancementRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for Gemini API integration.
 * Handles all AI-powered text enhancement and resume scoring operations.
 *
 * Features:
 * - Enhance resume content using Gemini API
 * - Generate resume improvement suggestions
 * - Calculate resume score based on content quality
 */
@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    @Autowired
    private GeminiConfig geminiConfig;

    @Autowired
    private RestTemplate restTemplate;

    private final Gson gson = new Gson();

    /**
     * Enhance resume content using Gemini API
     * Sends text to Gemini and receives professionally improved content
     */
    public String enhanceResumeContent(EnhancementRequestDTO request) {
        try {
            String prompt = buildEnhancementPrompt(request);
            String response = callGeminiAPI(prompt);
            log.info("Successfully enhanced resume content");
            return response;
        } catch (Exception e) {
            log.error("Error enhancing resume content: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to enhance resume: " + e.getMessage());
        }
    }

    /**
     * Calculate resume score using Gemini API
     * Analyzes resume quality and provides improvement suggestions
     */
    public String calculateResumeScore(EnhancementRequestDTO request) {
        try {
            String prompt = buildScoringPrompt(request);
            String response = callGeminiAPI(prompt);
            log.info("Successfully calculated resume score");
            return response;
        } catch (Exception e) {
            log.error("Error calculating resume score: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to calculate resume score: " + e.getMessage());
        }
    }

    /**
     * Call Gemini API with the given prompt
     */
    private String callGeminiAPI(String prompt) {
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
                throw new RuntimeException("Gemini API returned error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            throw new RuntimeException("Gemini API call failed: " + e.getMessage());
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
            
            throw new RuntimeException("Unexpected Gemini API response format");
        } catch (Exception e) {
            log.error("Error extracting text from Gemini response: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }

    /**
     * Build enhancement prompt for Gemini API
     */
    private String buildEnhancementPrompt(EnhancementRequestDTO request) {
        return "You are a professional resume writing expert. Rewrite and improve the following resume content " +
                "to sound more professional, concise, and impactful for a fresher. " +
                "Keep the meaning intact but make it more compelling.\n\n" +
                "Resume Content:\n" +
                request.getCombinedText() +
                "\n\nProvide the enhanced content in the following JSON format:\n" +
                "{\n" +
                "  \"enhancedCareerObjective\": \"...\",\n" +
                "  \"enhancedProfessionalSummary\": \"...\",\n" +
                "  \"enhancedSkills\": \"...\",\n" +
                "  \"enhancedProjects\": \"...\",\n" +
                "  \"enhancedAchievements\": \"...\"\n" +
                "}\n\n" +
                "Return ONLY valid JSON, no additional text.";
    }

    /**
     * Build scoring prompt for Gemini API
     */
    private String buildScoringPrompt(EnhancementRequestDTO request) {
        return "You are a resume evaluation expert. Analyze the following resume content and provide:\n" +
                "1. A score out of 100\n" +
                "2. Key strengths\n" +
                "3. Areas for improvement\n" +
                "4. Specific action items\n\n" +
                "Resume Content:\n" +
                request.getCombinedText() +
                "\n\nProvide the evaluation in the following JSON format:\n" +
                "{\n" +
                "  \"score\": <number>,\n" +
                "  \"strengths\": [\"...\", \"...\"],\n" +
                "  \"improvements\": [\"...\", \"...\"],\n" +
                "  \"actionItems\": [\"...\", \"...\"]\n" +
                "}\n\n" +
                "Return ONLY valid JSON, no additional text.";
    }
}
