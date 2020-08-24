package org.meeuw.math.numbers;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertaintyNumberOperations<N extends Number> extends NumberOperations<N> {


    default N multiplyUncertainty(N multiplier, N uncertainty) {
        return multiply(abs(multiplier),  uncertainty);
    }
}
