package com.darwin.abilityservice.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    TECHNOLOGY_NOT_FOUND("Technology not found: id - "),
    TECHNOLOGY_ID_IS_DUPLICATED("Technology id is duplicated."),
    ABILITY_NOT_FOUND("Ability not found.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
