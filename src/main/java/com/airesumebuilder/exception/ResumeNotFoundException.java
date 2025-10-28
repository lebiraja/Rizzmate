package com.airesumebuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resume is not found in the database.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResumeNotFoundException extends RuntimeException {
    
    public ResumeNotFoundException(Long id) {
        super("Resume not found with ID: " + id);
    }
    
    public ResumeNotFoundException(String email) {
        super("Resume not found for email: " + email);
    }
    
    public ResumeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
