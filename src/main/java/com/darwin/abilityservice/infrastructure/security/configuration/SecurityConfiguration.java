package com.darwin.abilityservice.infrastructure.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final ServerSecurityContextRepository serverSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/auth/login", "/swagger-ui.html", "/swagger-ui", "/swagger-ui/**", "/api-docs/**").permitAll()
                        .pathMatchers("/abilities/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .securityContextRepository(serverSecurityContextRepository)
                .build();
    }
}
