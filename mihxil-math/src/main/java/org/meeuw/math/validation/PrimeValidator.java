package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.meeuw.math.Utils;

public class PrimeValidator implements ConstraintValidator<Prime, Object> {

    @Override
    public void initialize(Prime constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        long toValidate = SquareValidator.toLong(value);
        return Utils.isPrime((int) toValidate);

    }
}
