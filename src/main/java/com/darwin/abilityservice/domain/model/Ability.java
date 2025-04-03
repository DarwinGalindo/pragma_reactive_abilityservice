package com.darwin.abilityservice.domain.model;

import java.util.List;

public class Ability {
    private Long id;
    private String name;
    private String description;
    private Integer technologiesCount;
    private List<Long> technologyIds;
    private List<Technology> technologyList;

    public Ability() {
    }

    public Ability(Long id) {
        this.id = id;
    }

    public Ability(Long id, String name, String description, Integer technologiesCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologiesCount = technologiesCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getTechnologyIds() {
        return technologyIds;
    }

    public void setTechnologyIds(List<Long> technologyIds) {
        this.technologyIds = technologyIds;
    }

    public Integer getTechnologiesCount() {
        return technologiesCount;
    }

    public void setTechnologiesCount(Integer technologiesCount) {
        this.technologiesCount = technologiesCount;
    }

    public List<Technology> getTechnologyList() {
        return technologyList;
    }

    public void setTechnologyList(List<Technology> technologyList) {
        this.technologyList = technologyList;
    }
}
