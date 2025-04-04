package com.darwin.abilityservice.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${microservices.tech-service.api-base}")
    private String techServiceApiBase;

    @Bean("technologies")
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(techServiceApiBase)
                .build();
    }
}
