package org.meeuw.math.numbers;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface SignedNumberElement<E extends SignedNumberElement<E>> extends NumberElement<E> {

      default boolean isNegative() {
        return compareTo(0d) < 0;
    }

    default boolean isPositive() {
        return compareTo(0d) > 0;
    }

    default boolean isZero() {
        return compareTo(0d) == 0;
    }
    default boolean isOne() {
        return compareTo(1d) == 0;
    }

    E negation();

    default E abs() {
        return isNegative() ? negation() : (E) this;
    }
}
