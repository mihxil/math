package org.meeuw.math.abstractalgebra;

/**
 * * A {@link FieldElement} that is is also a {@link Number} (which sadly is not an interface, so we can't extend it here).
 *
 * This means it has all converion methods like {@link #intValue()}, {@link #doubleValue()}.
 *
 * Also the method {@link #times(double)} can be defined then, to accomodate scalar multiplication.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberFieldElement<F extends NumberFieldElement<F>>   extends
    FieldElement<F>,
    Comparable<Number> {

    @Override
    NumberField<F> structure();

    F times(double multiplier);

    default <G extends NumberFieldElement<G>> F times(G multiplier) {
        return times(multiplier.doubleValue());
    }

    /**
     * Returns the value of the specified number as an {@code int},
     * which may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code int}.
     */
    int intValue();

    /**
     * Returns the value of the specified number as a {@code long},
     * which may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code long}.
     */
    long longValue();

    /**
     * Returns the value of the specified number as a {@code float},
     * which may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code float}.
     */
    float floatValue();

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

}
