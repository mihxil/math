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


    default N multipliedUncertainty(N newValue, N fractionUncertainty1, N fractionalUncertainty2) {
        return multiply(abs(newValue), sqrt(add(sqr(fractionUncertainty1), sqr(fractionalUncertainty2))));
    }

    default N addUncertainty(N uncertainty1, N uncertainty2) {
        return sqrt(add(multiply(uncertainty1, uncertainty1), multiply(uncertainty2, uncertainty2)));
    }

    default N powerUncertainty(N base, N baseUncertainty, N exponent, N exponentUncertainty) {
        //https://en.wikipedia.org/wiki/Propagation_of_uncertainty#Linear_combinations
        return multiply(abs(pow(base, exponent)), sqrt(
            add(sqr(divide(multiply(exponent, baseUncertainty), base)), sqr(multiply(ln(base), exponentUncertainty)))
        ));
    }

}
