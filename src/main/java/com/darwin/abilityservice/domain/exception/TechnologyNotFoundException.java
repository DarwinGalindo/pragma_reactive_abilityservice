package com.darwin.abilityservice.domain.exception;

public class TechnologyNotFoundException extends RuntimeException {
    private final Long id;

    public TechnologyNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
