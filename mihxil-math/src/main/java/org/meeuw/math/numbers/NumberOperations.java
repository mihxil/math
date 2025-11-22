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
package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.OptionalInt;

import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.exceptions.NotFiniteException;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * This interface generalises numeric operations for  {@link BigDecimal} and {@link Double}.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberOperations<N extends Number> {

    @SuppressWarnings("unchecked")
    static <N extends Number> NumberOperations<N> of(N n) {
        if (n instanceof BigDecimal) {
            return (NumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (NumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }
    static <N extends Number> NumberOperations<N> ofClass(Class<N> numberClass) {
        if (BigDecimal.class.isAssignableFrom(numberClass)) {
            return (NumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (NumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }

    N  getFractionalUncertainty(N value, N unc);

    N sqr(N v);

    UncertainNumber<N> sqrt(N radicand);

    UncertainNumber<N> root(N radicand, int i);


    N abs(N v);

    UncertainNumber<N> reciprocal(N v);

    N negate(N v);

    N multiply(N n1, N n2);

    @SuppressWarnings("unchecked")
    N multiply(N... n1);

    UncertainNumber<N> ln(N n) throws IllegalLogarithmException;

     N multiply(N n1, int n2);

    N multiply(N n1, Factor factor);

    default N scaleByPowerOfTen(N number, int exponent) {
        return scaleByPowerOfTenExact(number, exponent);
    }

    N scaleByPowerOfTenExact(N number, int exponent);


    default OptionalInt orderOfMagnitude(N in) {
        if (! isFinite(in)) {
            throw new NotFiniteException("not finite " + in);
        }
        if (isZero(in)) {
            return OptionalInt.empty();
        }

        N coeff = abs(in);
        int exponent = 0;

        if (!isZero(coeff)) {
            // coefficient >= 10: find minimal k so that coeff * 10^-k < 10
            if (gte(coeff, 10)) {
                int low = 0, high = 1;
                N tmp = scaleByPowerOfTen(coeff, -high);
                while (gte(tmp, 10)) {
                    low = high;
                    high <<= 1;
                    tmp = scaleByPowerOfTen(coeff, -high);
                }
                while (high - low > 1) {
                    int mid = low + ((high - low) >> 1);
                    N midTmp = scaleByPowerOfTen(coeff, -mid);
                    if (gte(midTmp, 10)) {
                        low = mid;
                    } else {
                        high = mid;
                    }
                }
                exponent += high;
            }

            // coefficient < 1: find minimal k so that coeff * 10^k >= 1
            if (lt(coeff, 1)) {
                int low = 0, high = 1;
                N tmp = scaleByPowerOfTen(coeff, high);
                while (lt(tmp, 1)) {
                    low = high;
                    high <<= 1;
                    tmp = scaleByPowerOfTenExact(coeff, high);
                }
                while (high - low > 1) {
                    int mid = low + ((high - low) >> 1);
                    N midTmp = scaleByPowerOfTen(coeff, mid);
                    if (lt(midTmp, 1)) {
                        low = mid;
                    } else {
                        high = mid;
                    }
                }
                exponent -= high;
            }
        }
        return OptionalInt.of(exponent);
    }

    N multiply(N n1, BigInteger n2);

    default UncertainNumber<N> multiply(N n2, int n, int d) {
        return divide(multiply(n2, n), d);
    }

    UncertainNumber<N> multiplyPrimitiveDouble(N n2, double d);

    UncertainNumber<N> divide(N n1, N n2);

    UncertainNumber<N> divide(N n1, int n2);

    N divideInt(N n1, int n2);

    N add(N n1, N n2);

    @SuppressWarnings("unchecked")
    N add(N... n);

    default N minus(N n1, N n2) {
        return add(n1, negate(n2));
    }

    N pow(N n1, int exponent);

    UncertainNumber<N> exp(N e);

    UncertainNumber<N> pow(N n1, N exponent);

    boolean lt(N n1, N n2);

    boolean lt(N n1, long i);

    boolean lte(N n1, N n2);

    default boolean gt(N n1, N n2) {
        return ! lte(n1, n2);
    }

    default boolean gte(N n1, N n2) {
        return ! lt(n1, n2);
    }
    boolean gte(N n1, long i);


    default int compare(N n1, N n2) {
        if (lt(n1, n2)) {
            return -1;
        } else if (gt(n1, n2)) {
            return 1;
        } else {
            return 0;
        }
    }

    @SuppressWarnings({"varargs", "unchecked"})
    default N max(N... n) {
        return Arrays.stream(n).max(this::compare).orElse(null);
    }


    @SuppressWarnings("unchecked")
    default N min(N... n) {
        return Arrays.stream(n).min(this::compare).orElse(null);
    }

    boolean isFinite(N n1);

    boolean isNaN(N n1);

    int signum(N n);

    BigDecimal bigDecimalValue(N n);

    UncertainNumber<N> sin(N n);

    UncertainNumber<N> asin(N n);

    UncertainNumber<N> cos(N n);

    UncertainNumber<N> tan(N n);

    UncertainNumber<N> atan2(N y, N x);


    boolean isZero(N n);

    N fromString(String s);

    N zero();

    /**
     * @since 0.19
     */
    int precision(N coefficient);
}
