package com.airesumebuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when Gemini API encounters an error.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class GeminiApiException extends RuntimeException {
    
    public GeminiApiException(String message) {
        super("Gemini API error: " + message);
    }
    
    public GeminiApiException(String message, Throwable cause) {
        super("Gemini API error: " + message, cause);
    }
}
