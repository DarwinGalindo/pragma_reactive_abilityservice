package com.darwin.abilityservice.domain.usecase;

import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import com.darwin.abilityservice.domain.exception.AbilityNotFoundException;
import com.darwin.abilityservice.domain.exception.TechnologyIdIsDuplicatedException;
import com.darwin.abilityservice.domain.exception.TechnologyNotFoundException;
import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.IAbilityTechnologyPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class AbilityUserCase implements IAbilityServicePort {
    private final IAbilityPersistencePort abilityPersistencePort;
    private final IAbilityTechnologyPersistencePort abilityTechnologyPersistencePort;
    private final ITechnologyWebClientPort technologyWebClientPort;

    public AbilityUserCase(IAbilityPersistencePort abilityPersistencePort,
                           IAbilityTechnologyPersistencePort abilityTechnologyPersistencePort,
                           ITechnologyWebClientPort technologyWebClientPort) {
        this.abilityPersistencePort = abilityPersistencePort;
        this.abilityTechnologyPersistencePort = abilityTechnologyPersistencePort;
        this.technologyWebClientPort = technologyWebClientPort;
    }

    @Override
    public Mono<Ability> create(Ability ability) {
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
                    return abilityPersistencePort.create(ability)
                            .flatMap(abilityDb -> Flux.fromIterable(technologyIds)
                                    .map(technologyId -> new AbilityTechnology(null, abilityDb.getId(), technologyId))
                                    .collectList()
                                    .flatMapMany(abilityTechnologyPersistencePort::create)
                                    .then(Mono.just(abilityDb)));
                });
    }

    @Override
    public Flux<Ability> paginate(int page, int size, String sortProperty, boolean sortAscending) {
        return abilityPersistencePort
                .paginate(page, size, sortProperty, sortAscending)
                .flatMap(ability -> abilityTechnologyPersistencePort
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
                .flatMap(ability -> abilityTechnologyPersistencePort.findAllByAbilityId(ability.getId())
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
