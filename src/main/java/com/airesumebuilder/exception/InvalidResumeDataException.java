package com.airesumebuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when resume data validation fails.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidResumeDataException extends RuntimeException {
    
    public InvalidResumeDataException(String message) {
        super(message);
    }
    
    public InvalidResumeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
