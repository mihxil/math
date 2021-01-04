package org.meeuw.math.numbers;

import java.math.BigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertaintyNumberOperations<N extends Number> extends NumberOperations<N> {


    static <N extends Number> UncertaintyNumberOperations<N> of(N n) {
        if (n instanceof BigDecimal) {
            return (UncertaintyNumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (UncertaintyNumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }

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
