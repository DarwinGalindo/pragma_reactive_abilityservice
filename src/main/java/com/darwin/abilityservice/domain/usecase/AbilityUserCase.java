package com.darwin.abilityservice.domain.usecase;

import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import com.darwin.abilityservice.domain.exception.AbilityNotFoundException;
import com.darwin.abilityservice.domain.exception.TechnologyIdIsDuplicatedException;
import com.darwin.abilityservice.domain.exception.TechnologyNotFoundException;
import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class AbilityUserCase implements IAbilityServicePort {
    private final IAbilityPersistencePort abilityPersistencePort;
    private final ITechnologyWebClientPort technologyWebClientPort;

    public AbilityUserCase(IAbilityPersistencePort abilityPersistencePort,
                           ITechnologyWebClientPort technologyWebClientPort) {
        this.abilityPersistencePort = abilityPersistencePort;
        this.technologyWebClientPort = technologyWebClientPort;
    }

    @Override
    public Mono<Ability> createAbility(Ability ability) {
        List<Long> technologyIds = ability.getTechnologyIds();

        if (technologyIds.size() != technologyIds.stream().distinct().count()) {
            return Mono.error(new TechnologyIdIsDuplicatedException());
        }

        return Flux.fromIterable(ability.getTechnologyIds())
                .flatMapSequential(id -> technologyWebClientPort.existsById(id)
                        .flatMap(exists -> exists.equals(Boolean.TRUE) ? Mono.just(true) : Mono.error(new TechnologyNotFoundException(id)))
                )
                .collectList()
                .flatMap(existing -> {
                    ability.setTechnologiesCount(technologyIds.size());
                    return abilityPersistencePort.createAbility(ability);
                });
    }

    @Override
    public Flux<Ability> filterAbilities(int page, int size, String sortProperty, boolean sortAscending) {
        return abilityPersistencePort
                .filterAbilities(page, size, sortProperty, sortAscending)
                .flatMap(ability -> abilityPersistencePort
                        .findAllByAbilityId(ability.getId())
                        .flatMap(abilityTechnology -> technologyWebClientPort
                                .findById(abilityTechnology.getTechnologyId()))
                        .collectList()
                        .map(technologyList -> {
                            ability.setTechnologyList(technologyList);
                            return ability;
                        }));
    }

    @Override
    public Mono<Ability> findById(Long id) {
        return abilityPersistencePort.findById(id)
                .flatMap(ability -> abilityPersistencePort.findAllByAbilityId(ability.getId())
                        .flatMap(abilityTechnology -> technologyWebClientPort
                                .findById(abilityTechnology.getTechnologyId()))
                        .collectList()
                        .map(technologyList -> {
                            ability.setTechnologyList(technologyList);
                            return ability;
                        }))
                .switchIfEmpty(Mono.error(new AbilityNotFoundException()));
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return abilityPersistencePort.existsById(id);
    }
}
