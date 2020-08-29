package org.meeuw.math.numbers;

/**
 * A number that also has a clearly defined concept of 'negativity'.
 *
 * E.g. a complex number is comparable but it is not 'signed'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface SignedNumber<E extends SignedNumber<E>> extends Numerical<E> {


    default boolean isNegative() {
        return signum() == -1;
    }

    default boolean isPositive() {
        return signum() ==  1;
    }

    default boolean isOne() {
        return compareTo(1d) == 0;
    }

    default boolean isZero() {
        return signum() == 0;
    }

    E negation();

    default E abs() {
        return isNegative() ? negation() : (E) this;
    }


    int signum();
}
