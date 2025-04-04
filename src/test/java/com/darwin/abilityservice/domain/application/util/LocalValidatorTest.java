package com.darwin.abilityservice.domain.application.util;

import com.darwin.abilityservice.application.validation.LocalValidator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.*;

class LocalValidatorTest {

    private LocalValidator localValidator;

    @BeforeEach
    void setUp() {
        var validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.afterPropertiesSet();

        localValidator = new LocalValidator(validatorFactoryBean);
    }

    @Test
    void validate_shouldNotThrowException_whenObjectIsValid() {
        TestDto valid = new TestDto("Valid Name", 10);
        assertDoesNotThrow(() -> localValidator.validate(valid));
    }

    @Test
    void validate_shouldThrowServerWebInputException_whenObjectIsInvalid() {
        TestDto invalid = new TestDto("", -1);

        var exception = assertThrows(ServerWebInputException.class, () -> localValidator.validate(invalid));

        String message = exception.getMessage();
        assertTrue(message.contains("must not be blank"));
        assertTrue(message.contains("must be greater than or equal to 0"));
    }

    private record TestDto(
            @NotBlank(message = "Name must not be blank") String name,
            @Min(value = 0, message = "Experience must be greater than or equal to 0") int experience
    ) {
    }
}