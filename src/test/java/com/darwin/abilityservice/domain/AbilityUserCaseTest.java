package com.darwin.abilityservice.domain;

import com.darwin.abilityservice.domain.exception.AbilityNotFoundException;
import com.darwin.abilityservice.domain.exception.TechnologyIdIsDuplicatedException;
import com.darwin.abilityservice.domain.exception.TechnologyNotFoundException;
import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.domain.model.Technology;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.IAbilityTechnologyPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import com.darwin.abilityservice.domain.usecase.AbilityUserCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class AbilityUserCaseTest {
    private IAbilityPersistencePort abilityPersistencePort;
    private IAbilityTechnologyPersistencePort abilityTechnologyPersistencePort;
    private ITechnologyWebClientPort technologyWebClientPort;
    private AbilityUserCase abilityUserCase;

    @BeforeEach
    void setUp() {
        abilityPersistencePort = mock(IAbilityPersistencePort.class);
        technologyWebClientPort = mock(ITechnologyWebClientPort.class);
        abilityTechnologyPersistencePort = mock(IAbilityTechnologyPersistencePort.class);
        abilityUserCase = new AbilityUserCase(abilityPersistencePort, abilityTechnologyPersistencePort, technologyWebClientPort);
    }

    @Test
    void create_shouldCreate() {
        List<Long> technologyIds = List.of(1L, 2L, 3L);

        Ability ability = new Ability(1L, "Backend", "Description", technologyIds.size());
        ability.setTechnologyIds(technologyIds);

        var abilityTechnologies = List.of(
                new AbilityTechnology(1L, 1L, 1L),
                new AbilityTechnology(2L, 1L, 2L),
                new AbilityTechnology(3L, 1L, 3L)
        );

        when(technologyWebClientPort.existsById(technologyIds.get(0))).thenReturn(Mono.just(true));
        when(technologyWebClientPort.existsById(technologyIds.get(1))).thenReturn(Mono.just(true));
        when(technologyWebClientPort.existsById(technologyIds.get(2))).thenReturn(Mono.just(true));
        when(abilityPersistencePort.create(ability)).thenReturn(Mono.just(ability));
        when(abilityTechnologyPersistencePort.create(any()))
                .thenReturn(Flux.fromIterable(abilityTechnologies));

        StepVerifier.create(abilityUserCase.create(ability))
                .expectNext(ability)
                .verifyComplete();

        verify(technologyWebClientPort, times(3)).existsById(anyLong());
        verify(abilityPersistencePort).create(ability);
    }

    @Test
    void create_shouldThrowNotFoundException_ifTechIdDoesNotExist() {
        List<Long> technologyIds = List.of(1L, 22L, 3L);

        Ability ability = new Ability(1L, "Backend", "Description", technologyIds.size());
        ability.setTechnologyIds(technologyIds);

        when(technologyWebClientPort.existsById(technologyIds.get(0))).thenReturn(Mono.just(true));
        when(technologyWebClientPort.existsById(technologyIds.get(1))).thenReturn(Mono.just(false));

        StepVerifier.create(abilityUserCase.create(ability))
                .expectError(TechnologyNotFoundException.class)
                .verify();

        verify(technologyWebClientPort, times(2)).existsById(anyLong());
        verify(abilityPersistencePort, never()).create(ability);
    }

    @Test
    void create_shouldThrowDuplicateException_ifTechIdIsDuplicated() {
        List<Long> technologyIds = List.of(1L, 2L, 2L);

        Ability ability = new Ability(1L, "Backend", "Description", technologyIds.size());
        ability.setTechnologyIds(technologyIds);

        StepVerifier.create(abilityUserCase.create(ability))
                .expectError(TechnologyIdIsDuplicatedException.class)
                .verify();

        verify(technologyWebClientPort, never()).findById(anyLong());
        verify(abilityPersistencePort, never()).create(ability);
    }

    @Test
    void paginate() {
        int page = 0;
        int size = 5;
        String sortProperty = "name";
        boolean sortAscending = true;

        List<AbilityTechnology> abilityTechnologies1 = List.of(
                new AbilityTechnology(1L, 1L, 1L),
                new AbilityTechnology(2L, 1L, 2L),
                new AbilityTechnology(3L, 1L, 3L)
        );

        List<AbilityTechnology> abilityTechnologies2 = List.of(
                new AbilityTechnology(4L, 2L, 1L),
                new AbilityTechnology(5L, 2L, 2L),
                new AbilityTechnology(6L, 2L, 3L)
        );

        List<Technology> existingTechs = List.of(
                new Technology(1L, "Java", "Desc"),
                new Technology(2L, "Spring", "Desc"),
                new Technology(3L, "JPA", "Desc")
        );

        Ability ability1 = new Ability();
        ability1.setId(1L);
        ability1.setName("Ability 1");
        ability1.setTechnologyList(existingTechs);

        Ability ability2 = new Ability();
        ability2.setId(2L);
        ability2.setName("Ability 2");
        ability2.setTechnologyList(existingTechs);

        when(abilityPersistencePort.paginate(page, size, sortProperty, sortAscending))
                .thenReturn(Flux.just(ability1, ability2));
        when(abilityTechnologyPersistencePort.findAllByAbilityId(ability1.getId()))
                .thenReturn(Flux.fromIterable(abilityTechnologies1));
        when(abilityTechnologyPersistencePort.findAllByAbilityId(ability2.getId()))
                .thenReturn(Flux.fromIterable(abilityTechnologies2));
        when(technologyWebClientPort.findById(existingTechs.get(0).getId()))
                .thenReturn(Mono.just(existingTechs.get(0)));
        when(technologyWebClientPort.findById(existingTechs.get(1).getId()))
                .thenReturn(Mono.just(existingTechs.get(1)));
        when(technologyWebClientPort.findById(existingTechs.get(2).getId()))
                .thenReturn(Mono.just(existingTechs.get(2)));

        StepVerifier.create(abilityUserCase.paginate(page, size, sortProperty, sortAscending))
                .expectNext(ability1)
                .expectNext(ability2)
                .verifyComplete();

        verify(abilityPersistencePort).paginate(page, size, sortProperty, sortAscending);
        verify(abilityTechnologyPersistencePort, times(2)).findAllByAbilityId(anyLong());
        verify(technologyWebClientPort, times(6)).findById(anyLong());
    }

    @Test
    void findById_shouldReturnAbility() {
        Long id = 1L;
        Ability ability = new Ability(id);

        List<Technology> existingTechs = List.of(
                new Technology(1L, "Java", "Desc"),
                new Technology(2L, "Spring", "Desc")
        );

        List<AbilityTechnology> abilityTechnologies = List.of(
                new AbilityTechnology(1L, id, existingTechs.get(0).getId()),
                new AbilityTechnology(2L, id, existingTechs.get(1).getId())
        );

        when(abilityPersistencePort.findById(id)).thenReturn(Mono.just(ability));
        when(abilityTechnologyPersistencePort.findAllByAbilityId(id)).thenReturn(Flux.fromIterable(abilityTechnologies));
        when(technologyWebClientPort.findById(1L)).thenReturn(Mono.just(existingTechs.get(0)));
        when(technologyWebClientPort.findById(2L)).thenReturn(Mono.just(existingTechs.get(1)));

        StepVerifier.create(abilityUserCase.findById(id))
                .expectNext(ability)
                .verifyComplete();

        verify(abilityPersistencePort).findById(id);
        verify(abilityTechnologyPersistencePort).findAllByAbilityId(id);
        verify(technologyWebClientPort, times(2)).findById(anyLong());
    }

    @Test
    void findById_shouldThrowNotFound() {
        Long id = 1111L;

        when(abilityPersistencePort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(abilityUserCase.findById(id))
                .expectError(AbilityNotFoundException.class)
                .verify();

        verify(abilityPersistencePort).findById(id);
    }

    @Test
    void existsById_shouldReturnTrue() {
        Long id = 1L;

        when(abilityPersistencePort.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(abilityUserCase.existsById(id))
                .expectNext(true)
                .verifyComplete();

        verify(abilityPersistencePort).existsById(id);
    }

    @Test
    void existsById_shouldReturnFalse() {
        Long id = 1111L;

        when(abilityPersistencePort.existsById(id)).thenReturn(Mono.just(false));

        StepVerifier.create(abilityUserCase.existsById(id))
                .expectNext(false)
                .verifyComplete();

        verify(abilityPersistencePort).existsById(id);
    }
}