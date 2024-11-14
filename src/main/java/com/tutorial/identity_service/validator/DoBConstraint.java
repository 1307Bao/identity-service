package com.tutorial.identity_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD}) // Where annotation can be defined
@Retention(RetentionPolicy.RUNTIME) // When annotation can be executed
@Constraint( // A class responsible for this annotation
        validatedBy = {DoBValidator.class}
)
public @interface DoBConstraint {
    String message() default "Invalid Date of Birth";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
