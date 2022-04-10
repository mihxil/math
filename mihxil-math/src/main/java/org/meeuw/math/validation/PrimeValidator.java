package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.meeuw.math.Utils;

public class PrimeValidator implements ConstraintValidator<Prime, Object> {
    boolean power;
    @Override
    public void initialize(Prime constraintAnnotation) {
        power = constraintAnnotation.power();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        long toValidate = SquareValidator.toLong(value);
        if (power) {
            return Utils.isPrimePower(toValidate);
        } else {
            return Utils.isPrime((int) toValidate);
        }

    }
}
