package org.meeuw.math.numbers;

import java.math.*;

import org.meeuw.math.abstractalgebra.StrictlyOrdered;

/**
 * A scalar is the closest thing to a {@link Number} interface
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <SELF> self reference
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
        return Math.round(doubleValue());
    }


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
        return (byte)intValue();
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
        return (short)intValue();
    }

    BigDecimal bigDecimalValue();


    default boolean isFinite() {
        return true;
    }

    default boolean isNaN() {
        return false;
    }
}
