package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.NotASquareException;
import org.meeuw.math.numbers.SizeableScalar;

public class SquareValidator implements ConstraintValidator<Square, Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        long toValidate;
        if (value instanceof SizeableScalar) {
            toValidate = ((SizeableScalar) value).longValue();
        } else if (value instanceof Number) {
            toValidate = ((Number) value).intValue();
        } else if (value.getClass().isArray()) {
            toValidate = ((Object[]) value).length;
        } else if (value instanceof Collection<?>) {
            toValidate = ((Collection) value).size();
        } else {
            throw new IllegalArgumentException();
        }
        try {
            Utils.sqrt(toValidate);
            return true;
        } catch (NotASquareException notASquareException) {
            return false;
        }
    }
}
