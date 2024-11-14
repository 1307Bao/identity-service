package com.tutorial.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DoBValidator implements ConstraintValidator<DoBConstraint, LocalDate> {

    private int min;

    // Initialize to get data of this annotation
    @Override
    public void initialize(DoBConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    // A function handle this data is true?
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) {
            return true;
        }

        long age = ChronoUnit.YEARS.between(localDate, LocalDate.now());

        return age >= min;
    }
}
