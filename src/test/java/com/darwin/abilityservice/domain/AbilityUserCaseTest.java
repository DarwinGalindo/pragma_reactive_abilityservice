package com.darwin.abilityservice.domain;

import com.darwin.abilityservice.domain.exception.TechnologyIdIsDuplicatedException;
import com.darwin.abilityservice.domain.exception.TechnologyNotFoundException;
import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.domain.model.Technology;
import com.darwin.abilityservice.domain.spi.IAbilityPersistencePort;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        Ability ability = new Ability(1L, "Backend", "Description");
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

        Ability ability = new Ability(1L, "Backend", "Description");
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

        Ability ability = new Ability(1L, "Backend", "Description");
        ability.setTechnologyIds(technologyIds);

        StepVerifier.create(abilityUserCase.createAbility(ability))
                .expectError(TechnologyIdIsDuplicatedException.class)
                .verify();

        verify(technologyWebClientPort, never()).findById(anyLong());
        verify(abilityPersistencePort, never()).createAbility(ability);
    }
}