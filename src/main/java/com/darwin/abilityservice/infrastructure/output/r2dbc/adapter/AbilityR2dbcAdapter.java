package com.darwin.abilityservice.infrastructure.output.r2dbc.adapter;

import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityTechnologyEntity;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityRepository;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbilityR2dbcAdapter implements IAbilityPersistencePort {
    private final IAbilityRepository abilityRepository;
    private final IAbilityTechnologyRepository abilityTechnologyRepository;
    private final AbilityEntityMapper abilityEntityMapper;

    @Override
    public Mono<Ability> createAbility(Ability ability) {
        return abilityRepository
                .save(abilityEntityMapper.toEntity(ability))
                .flatMap(abilityEntity -> {
                    var abilityTechnologyEntityList = ability.getTechnologyIds().stream()
                            .map(technologyId -> {
                                AbilityTechnologyEntity abilityTechnologyEntity = new AbilityTechnologyEntity();
                                abilityTechnologyEntity.setAbilityId(abilityEntity.getId());
                                abilityTechnologyEntity.setTechnologyId(technologyId);

                                return abilityTechnologyEntity;
                            })
                            .toList();

                    return abilityTechnologyRepository
                            .saveAll(abilityTechnologyEntityList)
                            .then(Mono.just(abilityEntityMapper.toModel(abilityEntity)));
                });
    }
}
