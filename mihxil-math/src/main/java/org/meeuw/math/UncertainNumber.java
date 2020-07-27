package org.meeuw.math;

import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * Also defines scalar operations.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainNumber<T extends UncertainNumber<T>> extends NumberFieldElement<UncertainNumber<?>, UncertainNumbers>, Comparable<Number> {

    @Override
    default UncertainNumbers structure() {
        return UncertainNumbers.INSTANCE;
    }

    double EXACT = -1d;

    double doubleValue();

    double getUncertainty();

    default boolean isExact() {
        return getUncertainty() < 0d;
    }

    @Override
    default T negation() {
        return times(-1d);
    }

    default T dividedBy(double divisor) {
        return times(1d / divisor);
    }

    T plus(double summand);

    default T minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     */
    T combined(T m);

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    @Override
    T times(double multiplier);

}
