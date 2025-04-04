package com.darwin.abilityservice.infrastructure.output.r2dbc.adapter;

import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.domain.spi.IAbilityTechnologyPersistencePort;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityTechnologyEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class AbilityTechnologyR2dbcAdapter implements IAbilityTechnologyPersistencePort {
    private final IAbilityTechnologyRepository abilityTechnologyRepository;
    private final AbilityTechnologyEntityMapper abilityTechnologyEntityMapper;

    @Override
    public Flux<AbilityTechnology> create(List<AbilityTechnology> abilityTechnologies) {
        return Flux.fromIterable(abilityTechnologies)
                .map(abilityTechnologyEntityMapper::toEntity)
                .collectList()
                .flatMapMany(abilityTechnologyEntities -> abilityTechnologyRepository
                        .saveAll(abilityTechnologyEntities)
                        .map(abilityTechnologyEntityMapper::toModel));
    }

    @Override
    public Flux<AbilityTechnology> findAllByAbilityId(Long abilityId) {
        return abilityTechnologyRepository
                .findAllByAbilityId(abilityId)
                .map(abilityTechnologyEntityMapper::toModel);
    }
}
