package com.darwin.abilityservice.domain.spi;

import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.model.AbilityTechnology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAbilityPersistencePort {
    Mono<Ability> createAbility(Ability ability);
    Flux<Ability> filterAbilities(int page, int size, String sortProperty, boolean sortAscending);
    Flux<AbilityTechnology> findAllByAbilityId(Long abilityId);
    Mono<Ability> findById(Long id);
}
