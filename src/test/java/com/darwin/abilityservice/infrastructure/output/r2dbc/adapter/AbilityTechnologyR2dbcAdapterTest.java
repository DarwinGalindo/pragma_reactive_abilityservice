package com.darwin.abilityservice.infrastructure.output.r2dbc.adapter;

import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityTechnologyEntity;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityTechnologyEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityTechnologyEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbilityTechnologyR2dbcAdapterTest {
    private IAbilityTechnologyEntityRepository abilityTechnologyEntityRepository;
    private AbilityTechnologyEntityMapper abilityTechnologyEntityMapper;
    private AbilityTechnologyR2dbcAdapter abilityTechnologyR2dbcAdapter;

    @BeforeEach
    void setUp() {
        abilityTechnologyEntityRepository = org.mockito.Mockito.mock(IAbilityTechnologyEntityRepository.class);
        abilityTechnologyEntityMapper = org.mockito.Mockito.mock(AbilityTechnologyEntityMapper.class);
        abilityTechnologyR2dbcAdapter = new AbilityTechnologyR2dbcAdapter(abilityTechnologyEntityRepository, abilityTechnologyEntityMapper);
    }

    @Test
    void create() {
        var abilityTechnology = new AbilityTechnology(1L, 2L, 3L);
        var abilityEntity = new AbilityTechnologyEntity(1L, 2L, 3L);

        when(abilityTechnologyEntityMapper.toEntity(abilityTechnology)).thenReturn(abilityEntity);
        when(abilityTechnologyEntityRepository.saveAll(List.of(abilityEntity))).thenReturn(Flux.just(abilityEntity));
        when(abilityTechnologyEntityMapper.toModel(abilityEntity)).thenReturn(abilityTechnology);

        StepVerifier.create(abilityTechnologyR2dbcAdapter.create(List.of(abilityTechnology)))
                .expectNext(abilityTechnology)
                .verifyComplete();

        verify(abilityTechnologyEntityMapper).toEntity(abilityTechnology);
        verify(abilityTechnologyEntityRepository).saveAll(List.of(abilityEntity));
        verify(abilityTechnologyEntityMapper).toModel(abilityEntity);
    }

    @Test
    void findAllByAbilityId() {
        var abilityId = 1L;
        var abilityTechnologyEntity = new AbilityTechnologyEntity(1L, 2L, 3L);
        var abilityTechnology = new AbilityTechnology(1L, 2L, 3L);

        when(abilityTechnologyEntityRepository.findAllByAbilityId(abilityId)).thenReturn(Flux.just(abilityTechnologyEntity));
        when(abilityTechnologyEntityMapper.toModel(abilityTechnologyEntity))
                .thenReturn(abilityTechnology);

        StepVerifier.create(abilityTechnologyR2dbcAdapter.findAllByAbilityId(abilityId))
                .expectNext(abilityTechnology)
                .verifyComplete();

        verify(abilityTechnologyEntityRepository).findAllByAbilityId(abilityId);
        verify(abilityTechnologyEntityMapper).toModel(abilityTechnologyEntity);
    }
}