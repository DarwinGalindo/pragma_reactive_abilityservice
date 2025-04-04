package com.darwin.abilityservice.infrastructure.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI createOpenAPI(
            @Value("${springdoc.title}") String title,
            @Value("${springdoc.description}") String description,
            @Value("${springdoc.version}") String version,
            @Value("${springdoc.contact-name}") String contactName,
            @Value("${springdoc.contact-email}") String contactEmail
    ) {
        var contact = new Contact();
        contact.setName(contactName);
        contact.email(contactEmail);

        var info = new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(contact);

        SecurityRequirement securityRequirement = new SecurityRequirement().
                addList("Bearer Authentication");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");

        Components components = new Components()
                .addSecuritySchemes("Bearer Authentication", securityScheme);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
