package com.darwin.abilityservice.infrastructure.output.r2dbc.adapter;

import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityEntity;
import com.darwin.abilityservice.infrastructure.output.r2dbc.mapper.AbilityEntityMapper;
import com.darwin.abilityservice.infrastructure.output.r2dbc.repository.IAbilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AbilityR2dbcAdapterTest {

    private IAbilityRepository abilityRepository;
    private AbilityEntityMapper abilityEntityMapper;
    private AbilityR2dbcAdapter abilityR2DbcAdapter;

    @BeforeEach
    void setUp() {
        abilityRepository = mock(IAbilityRepository.class);
        abilityEntityMapper = mock(AbilityEntityMapper.class);
        abilityR2DbcAdapter = new AbilityR2dbcAdapter(abilityRepository, abilityEntityMapper);
    }

    @Test
    void create() {
        var ability = new Ability(1L, "Ability", "Desc", 0);
        var abilityEntity = new AbilityEntity();
        abilityEntity.setId(1L);
        abilityEntity.setName("Ability");

        when(abilityEntityMapper.toEntity(ability)).thenReturn(abilityEntity);
        when(abilityRepository.save(abilityEntity)).thenReturn(Mono.just(abilityEntity));
        when(abilityEntityMapper.toModel(abilityEntity)).thenReturn(ability);

        StepVerifier.create(abilityR2DbcAdapter.create(ability))
                .expectNext(ability)
                .verifyComplete();

        verify(abilityEntityMapper).toEntity(ability);
        verify(abilityRepository).save(abilityEntity);
        verify(abilityEntityMapper).toModel(abilityEntity);
    }

    @Test
    void paginate() {
        var page = 0;
        var size = 2;
        var sortAscending = true;
        var sortProperty = "name";
        var sort = Sort.by(Sort.Direction.ASC, sortProperty);
        var pageable = PageRequest.of(page, size, sort);

        var abilityEntity1 = new AbilityEntity(1L, "Java", "Programming Language", 0);
        var abilityEntity2 = new AbilityEntity(2L, "Python", "Programming Language", 0);

        var ability1 = new Ability(abilityEntity1.getId(),
                abilityEntity1.getName(), abilityEntity1.getDescription(), abilityEntity1.getTechnologiesCount());

        var ability2 = new Ability(abilityEntity2.getId(),
                abilityEntity2.getName(), abilityEntity2.getDescription(), abilityEntity2.getTechnologiesCount());

        when(abilityRepository.findBy(pageable)).thenReturn(Flux.just(abilityEntity1, abilityEntity2));
        when(abilityEntityMapper.toModel(abilityEntity1)).thenReturn(ability1);
        when(abilityEntityMapper.toModel(abilityEntity2)).thenReturn(ability2);

        StepVerifier.create(abilityR2DbcAdapter.paginate(page, size, sortProperty, sortAscending))
                .expectNext(ability1)
                .expectNext(ability2)
                .verifyComplete();

        verify(abilityRepository).findBy(pageable);
    }

    @Test
    void findById() {
        var id = 1L;
        var abilityEntity = new AbilityEntity(id, "Tech 1", "Desc", 0);
        var ability = new Ability(id, abilityEntity.getName(), abilityEntity.getDescription(), 0);

        when(abilityEntityMapper.toModel(abilityEntity)).thenReturn(ability);
        when(abilityRepository.findById(id)).thenReturn(Mono.just(abilityEntity));

        StepVerifier.create(abilityR2DbcAdapter.findById(id))
                .expectNext(ability)
                .verifyComplete();

        verify(abilityRepository).findById(id);
        verify(abilityEntityMapper).toModel(abilityEntity);
    }

    @Test
    void existsById() {
        Long id = 1L;

        when(abilityR2DbcAdapter.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(abilityR2DbcAdapter.existsById(id))
                .expectNext(true)
                .verifyComplete();

        verify(abilityRepository).existsById(id);
    }
}