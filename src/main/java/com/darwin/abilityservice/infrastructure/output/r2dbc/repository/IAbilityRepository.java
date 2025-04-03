package com.darwin.abilityservice.infrastructure.output.r2dbc.repository;

import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface IAbilityRepository extends ReactiveCrudRepository<AbilityEntity, Long> {
    Flux<AbilityEntity> findBy(Pageable pageable);
}
