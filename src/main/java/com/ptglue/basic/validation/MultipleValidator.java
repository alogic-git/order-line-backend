package com.ptglue.basic.validation;

import com.ptglue.basic.annotation.Multiple;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultipleValidator implements ConstraintValidator<Multiple, Integer> {

    private int value;

    @Override
    public void initialize(Multiple constraintAnnotation) {
        value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer number, ConstraintValidatorContext context) {
        return number != null && number % value == 0;
    }
}
