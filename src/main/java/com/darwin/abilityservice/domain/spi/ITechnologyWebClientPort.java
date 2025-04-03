package com.darwin.abilityservice.domain.spi;

import com.darwin.abilityservice.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyWebClientPort {
    Mono<Technology> findById(Long id);
}
