package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.NotASquareException;
import org.meeuw.math.numbers.SizeableScalar;

public class SquareValidator implements ConstraintValidator<Square, Object> {
    private int dimension = -1;

    @Override
    public void initialize(Square constraintAnnotation) {
        dimension = constraintAnnotation.dimension();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        long toValidate;
        if (value instanceof SizeableScalar) {
            toValidate = ((SizeableScalar) value).longValue();
        } else if (value instanceof Number) {
            toValidate = ((Number) value).intValue();
        } else if (value.getClass().isArray()) {
            Class<?> aClass = value.getClass().getComponentType();
            if (aClass.isArray()) {
                Object[][] v = (Object[][])  value;
                toValidate = 0;
                for (Object[] sv : v) {
                    if (sv.length != v.length) {
                        return false;
                    }
                    toValidate += sv.length;
                }
            } else {
                toValidate = ((Object[]) value).length;
            }
        } else if (value instanceof Collection<?>) {
            toValidate = ((Collection) value).size();
        } else {
            throw new IllegalArgumentException();
        }
        try {
            long sqrt = Utils.sqrt(toValidate);
            if (dimension >= 0) {
                return dimension == sqrt;
            }
            return true;
        } catch (NotASquareException notASquareException) {
            return false;
        }
    }
}
