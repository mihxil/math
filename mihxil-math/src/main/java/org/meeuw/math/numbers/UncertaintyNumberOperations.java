package org.meeuw.math.numbers;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertaintyNumberOperations<N extends Number> extends NumberOperations<N> {

    default N multiplyUncertainty(N multiplier, N uncertainty) {
        return multiply(abs(multiplier),  uncertainty);
    }

    default N addUncertainty(N uncertainty1, N uncertainty2) {
        return sqrt(add(multiply(uncertainty1, uncertainty1), multiply(uncertainty2, uncertainty2)));
    }

    default N powerUncertainty(N base, N baseUncertainty, N exponent, N exponentUncertainty) {
        return multiply(baseUncertainty, exponent);
    }

}
