package com.darwin.abilityservice.domain.spi;

import com.darwin.abilityservice.domain.model.Ability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAbilityPersistencePort {
    Mono<Ability> create(Ability ability);

    Flux<Ability> paginate(int page, int size, String sortProperty, boolean sortAscending);

    Mono<Ability> findById(Long id);

    Mono<Boolean> existsById(Long id);
}
