package com.darwin.abilityservice.infrastructure.configuration;

import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import com.darwin.abilityservice.domain.usecase.AbilityUserCase;
import com.darwin.abilityservice.infrastructure.output.r2dbc.adapter.AbilityR2dbcAdapter;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityTechnologyEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityRepository;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityTechnologyRepository;
import com.darwin.abilityservice.infrastructure.output.webclient.adapter.TechnologyWebClientAdapter;
import com.darwin.abilityservice.infrastructure.output.webclient.mapper.TechnologyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IAbilityRepository abilityRepository;
    private final IAbilityTechnologyRepository abilityTechnologyRepository;
    private final AbilityEntityMapper abilityEntityMapper;
    private final AbilityTechnologyEntityMapper abilityTechnologyEntityMapper;
    private final WebClient technologiesWebClient;
    private final TechnologyDtoMapper technologyDtoMapper;

    @Bean
    public IAbilityServicePort abilityServicePort() {
        return new AbilityUserCase(abilityPersistencePort(), technologyWebClientPort());
    }

    @Bean
    public IAbilityPersistencePort abilityPersistencePort() {
        return new AbilityR2dbcAdapter(abilityRepository, abilityTechnologyRepository,
                abilityEntityMapper, abilityTechnologyEntityMapper);
    }

    @Bean
    public ITechnologyWebClientPort technologyWebClientPort() {
        return new TechnologyWebClientAdapter(technologiesWebClient, technologyDtoMapper);
    }
}
