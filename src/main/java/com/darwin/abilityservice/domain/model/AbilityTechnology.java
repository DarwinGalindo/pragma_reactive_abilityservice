package com.darwin.abilityservice.domain.model;

public class AbilityTechnology {
    private Long id;
    private Long abilityId;
    private Long technologyId;

    public AbilityTechnology() {
    }

    public AbilityTechnology(Long id, Long abilityId, Long technologyId) {
        this.id = id;
        this.abilityId = abilityId;
        this.technologyId = technologyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(Long abilityId) {
        this.abilityId = abilityId;
    }

    public Long getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }
}
