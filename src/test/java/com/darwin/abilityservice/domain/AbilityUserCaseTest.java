package com.darwin.abilityservice.domain;

import com.darwin.abilityservice.domain.exception.TechnologyIdIsDuplicatedException;
import com.darwin.abilityservice.domain.exception.TechnologyNotFoundException;
import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.domain.model.Technology;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class AbilityUserCaseTest {
    private IAbilityPersistencePort abilityPersistencePort;
    private ITechnologyWebClientPort technologyWebClientPort;
    private AbilityUserCase abilityUserCase;

    @BeforeEach
    void setUp() {
        abilityPersistencePort = mock(IAbilityPersistencePort.class);
        technologyWebClientPort = mock(ITechnologyWebClientPort.class);
        abilityUserCase = new AbilityUserCase(abilityPersistencePort, technologyWebClientPort);
    }

    @Test
    void createAbility_shouldCreate() {
        List<Technology> existingTechs = List.of(
                new Technology(1L, "Java", "Desc"),
                new Technology(2L, "Spring", "Desc"),
                new Technology(3L, "JPA", "Desc")
        );
        List<Long> technologyIds = List.of(1L, 2L, 3L);

        Ability ability = new Ability(1L, "Backend", "Description", technologyIds.size());
        ability.setTechnologyIds(technologyIds);

        when(technologyWebClientPort.findById(technologyIds.get(0))).thenReturn(Mono.just(existingTechs.get(0)));
        when(technologyWebClientPort.findById(technologyIds.get(1))).thenReturn(Mono.just(existingTechs.get(1)));
        when(technologyWebClientPort.findById(technologyIds.get(2))).thenReturn(Mono.just(existingTechs.get(2)));
        when(abilityPersistencePort.createAbility(ability)).thenReturn(Mono.just(ability));

        StepVerifier.create(abilityUserCase.createAbility(ability))
                .expectNext(ability)
                .verifyComplete();

        verify(technologyWebClientPort, times(3)).findById(anyLong());
        verify(abilityPersistencePort).createAbility(ability);
    }

    @Test
    void createAbility_shouldThrowNotFoundException_ifTechIdDoesNotExist() {
        List<Technology> existingTechs = List.of(
                new Technology(1L, "Java", "Desc"),
                new Technology(2L, "Spring", "Desc"),
                new Technology(3L, "JPA", "Desc")
        );
        List<Long> technologyIds = List.of(1L, 22L, 3L);

        Ability ability = new Ability(1L, "Backend", "Description", technologyIds.size());
        ability.setTechnologyIds(technologyIds);

        when(technologyWebClientPort.findById(technologyIds.get(0))).thenReturn(Mono.just(existingTechs.get(0)));
        when(technologyWebClientPort.findById(technologyIds.get(1))).thenReturn(Mono.just(new Technology()));

        StepVerifier.create(abilityUserCase.createAbility(ability))
                .expectError(TechnologyNotFoundException.class)
                .verify();

        verify(technologyWebClientPort, times(2)).findById(anyLong());
        verify(abilityPersistencePort, never()).createAbility(ability);
    }

    @Test
    void createAbility_shouldThrowDuplicateException_ifTechIdIsDuplicated() {
        List<Long> technologyIds = List.of(1L, 2L, 2L);

        Ability ability = new Ability(1L, "Backend", "Description", technologyIds.size());
        ability.setTechnologyIds(technologyIds);

        StepVerifier.create(abilityUserCase.createAbility(ability))
                .expectError(TechnologyIdIsDuplicatedException.class)
                .verify();

        verify(technologyWebClientPort, never()).findById(anyLong());
        verify(abilityPersistencePort, never()).createAbility(ability);
    }

    @Test
    void filterAbilities() {
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
        ability1.setTechnologyList(existingTechs);

        when(abilityPersistencePort.filterAbilities(page, size, sortProperty, sortAscending))
                .thenReturn(Flux.just(ability1, ability2));
        when(abilityPersistencePort.findAllByAbilityId(ability1.getId()))
                .thenReturn(Flux.fromIterable(abilityTechnologies1));
        when(abilityPersistencePort.findAllByAbilityId(ability2.getId()))
                .thenReturn(Flux.fromIterable(abilityTechnologies2));
        when(technologyWebClientPort.findById(existingTechs.get(0).getId()))
                .thenReturn(Mono.just(existingTechs.get(0)));
        when(technologyWebClientPort.findById(existingTechs.get(1).getId()))
                .thenReturn(Mono.just(existingTechs.get(1)));
        when(technologyWebClientPort.findById(existingTechs.get(2).getId()))
                .thenReturn(Mono.just(existingTechs.get(2)));

        StepVerifier.create(abilityUserCase.filterAbilities(page, size, sortProperty, sortAscending))
                .expectNext(ability1)
                .expectNext(ability2)
                .verifyComplete();

        verify(abilityPersistencePort).filterAbilities(page, size, sortProperty, sortAscending);
        verify(abilityPersistencePort, times(2)).findAllByAbilityId(anyLong());
        verify(technologyWebClientPort, times(6)).findById(anyLong());
    }
}