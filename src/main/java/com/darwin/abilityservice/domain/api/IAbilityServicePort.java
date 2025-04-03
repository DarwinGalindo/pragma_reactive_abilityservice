package com.darwin.abilityservice.domain.api;

import com.darwin.abilityservice.domain.model.Ability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAbilityServicePort {
    Mono<Ability> createAbility(Ability ability);
    Flux<Ability> filterAbilities(int page, int size, String sortProperty, boolean sortAscending);
}
