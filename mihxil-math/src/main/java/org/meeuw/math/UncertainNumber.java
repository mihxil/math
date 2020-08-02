package org.meeuw.math;

import org.meeuw.math.abstractalgebra.NumberElement;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * Also defines scalar operations.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainNumber<T extends UncertainNumber<T>> extends NumberElement<T> {

    double EXACT = 0d;

    @Override
    double doubleValue();

    double getUncertainty();

    default boolean isExact() {
        return getUncertainty() == EXACT;
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
    T times(double multiplier);

}
