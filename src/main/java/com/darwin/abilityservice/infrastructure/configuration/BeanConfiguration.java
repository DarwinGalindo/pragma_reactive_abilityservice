package com.darwin.abilityservice.infrastructure.configuration;

import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.IAbilityTechnologyPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import com.darwin.abilityservice.domain.usecase.AbilityUserCase;
import com.darwin.abilityservice.infrastructure.output.r2dbc.adapter.AbilityR2dbcAdapter;
import com.darwin.abilityservice.infrastructure.output.r2dbc.adapter.AbilityTechnologyR2dbcAdapter;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityTechnologyEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityEntityRepository;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityTechnologyEntityRepository;
import com.darwin.abilityservice.infrastructure.output.webclient.adapter.TechnologyWebClientAdapter;
import com.darwin.abilityservice.infrastructure.output.webclient.mapper.TechnologyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IAbilityEntityRepository abilityRepository;
    private final IAbilityTechnologyEntityRepository abilityTechnologyRepository;
    private final AbilityEntityMapper abilityEntityMapper;
    private final AbilityTechnologyEntityMapper abilityTechnologyEntityMapper;
    private final WebClient technologiesWebClient;
    private final TechnologyDtoMapper technologyDtoMapper;

    @Bean
    public IAbilityServicePort abilityServicePort() {
        return new AbilityUserCase(abilityPersistencePort(), abilityTechnologyPersistencePort(), technologyWebClientPort());
    }

    @Bean
    public IAbilityPersistencePort abilityPersistencePort() {
        return new AbilityR2dbcAdapter(abilityRepository, abilityEntityMapper);
    }

    @Bean
    public IAbilityTechnologyPersistencePort abilityTechnologyPersistencePort() {
        return new AbilityTechnologyR2dbcAdapter(abilityTechnologyRepository, abilityTechnologyEntityMapper);
    }

    @Bean
    public ITechnologyWebClientPort technologyWebClientPort() {
        return new TechnologyWebClientAdapter(technologiesWebClient, technologyDtoMapper);
    }
}
