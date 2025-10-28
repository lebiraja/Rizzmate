package com.airesumebuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when PDF generation fails.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PdfGenerationException extends RuntimeException {
    
    public PdfGenerationException(String message) {
        super("PDF generation failed: " + message);
    }
    
    public PdfGenerationException(String message, Throwable cause) {
        super("PDF generation failed: " + message, cause);
    }
}
