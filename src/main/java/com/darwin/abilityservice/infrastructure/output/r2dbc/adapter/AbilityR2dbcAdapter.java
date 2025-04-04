package com.darwin.abilityservice.infrastructure.output.r2dbc.adapter;

import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbilityR2dbcAdapter implements IAbilityPersistencePort {
    private final IAbilityEntityRepository abilityRepository;
    private final AbilityEntityMapper abilityEntityMapper;

    @Override
    public Mono<Ability> create(Ability ability) {
        return abilityRepository
                .save(abilityEntityMapper.toEntity(ability))
                .map(abilityEntityMapper::toModel);
    }

    @Override
    public Flux<Ability> paginate(int page, int size, String sortProperty, boolean sortAscending) {
        Sort sort = Sort.by(sortAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortProperty);
        Pageable pageable = PageRequest.of(page, size, sort);

        return abilityRepository.findBy(pageable)
                .map(abilityEntityMapper::toModel);
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
