package org.meeuw.math.numbers;

import java.math.BigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertaintyNumberOperations<N extends Number> extends NumberOperations<N> {


    @SuppressWarnings("unchecked")
    static <N extends Number> UncertaintyNumberOperations<N> of(N n) {
        if (n instanceof BigDecimal) {
            return (UncertaintyNumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (UncertaintyNumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }


    /**
     * The uncertaintity
     */
    default N multipliedUncertainty(N newValue, N fractionUncertainty1, N fractionalUncertainty2) {
        return multiply(abs(newValue), sqrt(add(sqr(fractionUncertainty1), sqr(fractionalUncertainty2))).getValue());
    }

    default N addUncertainty(N uncertainty1, N uncertainty2) {
        return sqrt(add(sqr(uncertainty1), sqr(uncertainty2))).getValue();
    }

    N roundingUncertainty(N n);

    default N powerUncertainty(
        N base, N baseUncertainty,
        N exponent, N exponentUncertainty,
        N result) {
        //https://en.wikipedia.org/wiki/Propagation_of_uncertainty#Linear_combinations
        if (isZero(base) && isZero(baseUncertainty)) {
            return base;
        }
        base = max(base, roundingUncertainty(base));
        return multiply(
            abs(result),
            sqrt(
                add(
                    sqr(divide(multiply(exponent, baseUncertainty), base).getValue()),
                    sqr(multiply(ln(base).getValue(), exponentUncertainty))
                )
            ).getValue()
        );
    }

}
