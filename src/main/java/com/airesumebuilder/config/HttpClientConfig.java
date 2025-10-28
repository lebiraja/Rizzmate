package com.airesumebuilder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for HTTP clients and bean definitions.
 * Provides beans for RestTemplate and WebClient used for API calls.
 */
@Configuration
public class HttpClientConfig {

    /**
     * Create RestTemplate bean for synchronous HTTP calls
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Create WebClient bean for asynchronous HTTP calls
     */
    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}
