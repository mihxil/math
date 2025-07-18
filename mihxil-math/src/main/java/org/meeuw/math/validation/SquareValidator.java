/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.Collection;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.exceptions.NotASquareException;
import org.meeuw.math.numbers.SizeableScalar;

/**
 * Implementation for {@link Square}
 */
public class SquareValidator implements ConstraintValidator<Square, Object> {

    @Setter
    private int dimension = -1;
    @Setter
    private boolean invertible = false;

    @Override
    public void initialize(Square constraintAnnotation) {
        setDimension(constraintAnnotation.value());
        setInvertible(constraintAnnotation.invertible());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            long toValidate = toLong(value);
            long sqrt = IntegerUtils.sqrt(toValidate);
            if (dimension >= 0) {
                return dimension == sqrt;
            }
            if (invertible) {
                if (! value.getClass().isArray()) {
                    throw new IllegalArgumentException("Only square arrays can be inverted");
                }
                Class<?> aClass = value.getClass().getComponentType();
                if (aClass.isArray()) {
                    Class<?> bClass = aClass.getComponentType();
                    if (RingElement.class.isAssignableFrom(bClass)) {
                        RingElement<?> z = determinant((Object[][]) value);
                        return !z.isZero();
                    } else {
                        throw new IllegalArgumentException("Elements of " + value + " must be RingElements");
                    }
                } else {
                    throw new IllegalArgumentException("Only square arrays can be inverted");
                }

            }
            return true;
        } catch (NotASquareException notASquareException) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    static <E extends RingElement<E>> E determinant(Object[][] array) {
        E[][] casted = (E[][]) array;
        return casted[0][0].getStructure().determinant(casted);
    }

    @SuppressWarnings("rawtypes")
    static long toLong(Object value) {
        long toValidate;
         if (value instanceof SizeableScalar) {
             toValidate = ((SizeableScalar) value).longValue();
         } else if (value instanceof Byte) {
            toValidate = Byte.toUnsignedLong((Byte) value);
         } else if (value instanceof Integer) {
            toValidate = Integer.toUnsignedLong((Integer) value);
        } else if (value instanceof Number) {
            toValidate = ((Number) value).longValue();
        } else if (value.getClass().isArray()) {
            Class<?> aClass = value.getClass().getComponentType();
            final Object[] arrayValue = (Object[]) value;
            if (aClass.isArray()) {

                toValidate = 0;
                for (Object sv : arrayValue) {
                    var length = Array.getLength(sv);
                    if (length != arrayValue.length) {
                        throw new NotASquareException("not a square");
                    }
                    toValidate += length;
                }
            } else {
                toValidate = ((Object[]) value).length;
            }
        } else if (value instanceof Collection<?>) {
            toValidate = ((Collection) value).size();
        } else {
            throw new IllegalArgumentException();
        }
        return toValidate;
    }

}
