package com.darwin.abilityservice.application.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LocalValidator {
    private static final String OBJECT_NAME = "object";

    private final Validator validator;

    public <T> void validate(T object) {
        Errors errors = new BeanPropertyBindingResult(object, OBJECT_NAME);
        validator.validate(object, errors);

        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            String errorMessage = String.join(", ", errorMessages);
            throw new ServerWebInputException(errorMessage);
        }
    }
}
