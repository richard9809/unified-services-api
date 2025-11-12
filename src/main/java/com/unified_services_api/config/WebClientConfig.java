package com.unified_services_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient nominatimClient(@Value("${app.external.nominatimBase}") String base) {
        return WebClient.builder()
                .baseUrl(base)
                .defaultHeader(HttpHeaders.USER_AGENT, "UnifiedServicesAPI/1.0 (contact: you@example.com)")
                .build();
    }

    @Bean
    public WebClient openMeteoClient(@Value("${app.external.openMeteoBase}") String base) {
        return WebClient.builder().baseUrl(base).build();
    }
}
