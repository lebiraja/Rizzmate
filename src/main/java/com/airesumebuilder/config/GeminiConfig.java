package com.airesumebuilder.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class for Gemini API settings.
 * Maps properties from application.properties file.
 */
@Component
@ConfigurationProperties(prefix = "gemini.api")
public class GeminiConfig {
    private String key;
    private String endpoint;

    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
}
