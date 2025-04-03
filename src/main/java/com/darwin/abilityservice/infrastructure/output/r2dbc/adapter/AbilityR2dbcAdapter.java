package com.darwin.abilityservice.infrastructure.output.r2dbc.adapter;

import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityTechnologyEntity;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityTechnologyEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityRepository;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbilityR2dbcAdapter implements IAbilityPersistencePort {
    private final IAbilityRepository abilityRepository;
    private final IAbilityTechnologyRepository abilityTechnologyRepository;
    private final AbilityEntityMapper abilityEntityMapper;
    private final AbilityTechnologyEntityMapper abilityTechnologyEntityMapper;

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

    @Override
    public Flux<Ability> filterAbilities(int page, int size, String sortProperty, boolean sortAscending) {
        Sort sort = Sort.by(sortAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortProperty);
        Pageable pageable = PageRequest.of(page, size, sort);

        return abilityRepository.findBy(pageable)
                .map(abilityEntityMapper::toModel);
    }

    @Override
    public Flux<AbilityTechnology> findAllByAbilityId(Long abilityId) {
        return abilityTechnologyRepository.findAllByAbilityId(abilityId)
                .map(abilityTechnologyEntityMapper::toModel);
    }

    @Override
    public Mono<Ability> findById(Long id) {
        return abilityRepository.findById(id)
                .map(abilityEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return abilityRepository.existsById(id);
    }
}
