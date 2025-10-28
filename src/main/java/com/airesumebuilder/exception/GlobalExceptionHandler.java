package com.airesumebuilder.exception;

import com.airesumebuilder.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all REST controllers.
 * Provides consistent error responses across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResumeNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResumeNotFound(ResumeNotFoundException ex) {
        log.error("Resume not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error("Resume not found", ex.getMessage()));
    }
    
    @ExceptionHandler(GeminiApiException.class)
    public ResponseEntity<ApiResponse<String>> handleGeminiApiError(GeminiApiException ex) {
        log.error("Gemini API error", ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ApiResponse.error("AI service temporarily unavailable", 
                  "Please try again later. The AI service is experiencing issues."));
    }
    
    @ExceptionHandler(InvalidResumeDataException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidResumeData(InvalidResumeDataException ex) {
        log.error("Invalid resume data: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error("Invalid resume data", ex.getMessage()));
    }
    
    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<ApiResponse<String>> handlePdfGenerationError(PdfGenerationException ex) {
        log.error("PDF generation error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("PDF generation failed", 
                  "Unable to generate PDF. Please try again or contact support."));
    }
    
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleRateLimitExceeded(RateLimitExceededException ex) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body(ApiResponse.error("Rate limit exceeded", ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.error("Validation errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error("Validation failed", errors));
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpClientError(HttpClientErrorException ex) {
        log.error("HTTP client error: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode())
            .body(ApiResponse.error("External service error", 
                  "Failed to communicate with external service"));
    }
    
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpServerError(HttpServerErrorException ex) {
        log.error("HTTP server error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ApiResponse.error("External service unavailable", 
                  "External service is temporarily unavailable"));
    }
    
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponse<String>> handleRestClientError(RestClientException ex) {
        log.error("REST client error", ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ApiResponse.error("Service communication error", 
                  "Unable to communicate with external service"));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericError(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("Internal server error", 
                  "An unexpected error occurred. Please try again or contact support."));
    }
}
