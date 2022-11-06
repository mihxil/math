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

import java.math.*;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;

/**
 * A sizable is a {@link Scalar},  but it adds methods related to {@link Sizeable}, {@link SignedNumber} and {@link StrictlyOrdered}
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <SELF> self reference
 * @param <SIZE> the type of the 'size' of the scalar. in many cases the same as {@code SELF}, but not always. E.g. the size of {@code NegativeInteger} is {@code PositiveInteger}
 */
public interface SizeableScalar<SELF extends SizeableScalar<SELF, SIZE>, SIZE extends Scalar<SIZE>>
    extends
    SignedNumber<SELF>,
    Sizeable<SIZE>,
    StrictlyOrdered<SELF>
{

    /**
     * Returns the value of the specified number as an {@code int},
     * which may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code int}.
     */
    default int intValue() {
        return (int) longValue();
    }

    /**
     * Returns the value of the specified number as a {@code long},
     * which may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code long}.
     */
    default long longValue() {
        return Utils.round(doubleValue());
    }


    /**
     * Returns the value of the specified number as a {@link BigInteger},
     * which may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@link BigInteger}.
     */
    default BigInteger bigIntegerValue() {
        return bigDecimalValue().setScale(0, RoundingMode.HALF_UP).toBigIntegerExact();
    }


    /**
     * Returns the value of the specified number as a {@code float},
     * which may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code float}.
     */
    default float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Returns the value of the specified number as a {@code double},
     * which may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code double}.
     */
    double doubleValue();

    /**
     * Returns the value of the specified number as a {@code byte},
     * which may involve rounding or truncation.
     *
     * <p>This implementation returns the result of {@link #intValue} cast
     * to a {@code byte}.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code byte}.
     */
    default byte byteValue() {
        return (byte)longValue();
    }

    /**
     * Returns the value of the specified number as a {@code short},
     * which may involve rounding or truncation.
     *
     * <p>This implementation returns the result of {@link #intValue} cast
     * to a {@code short}.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code short}.
     */
    default short shortValue() {
        return (short)longValue();
    }

    /**
     * Returns the value of the specified number as a {@link BigDecimal},
     * which may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@link BigInteger}.
     */
    BigDecimal bigDecimalValue();


    default boolean isFinite() {
        return true;
    }

    default boolean isNaN() {
        return false;
    }
}
