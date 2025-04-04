package com.darwin.abilityservice.domain.spi;

import com.darwin.abilityservice.domain.model.AbilityTechnology;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IAbilityTechnologyPersistencePort {
    Flux<AbilityTechnology> create(List<AbilityTechnology> abilityTechnologies);
    Flux<AbilityTechnology> findAllByAbilityId(Long abilityId);
}
