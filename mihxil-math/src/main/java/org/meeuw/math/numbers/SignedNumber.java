package org.meeuw.math.numbers;

/**
 * An object with a clearly defined concept of 'negativity'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface SignedNumber<SELF extends SignedNumber<SELF>> extends Comparable<SELF> {

    int signum();

    default boolean isPositive() {
        return signum() > 0;
    }

    default boolean isNegative() {
        return signum() < 0;
    }

    default boolean isZero() {
        return signum() == 0;
    }

}
