package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.util.function.Supplier;

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
        return uncertaintyContext(
            () -> multiply(abs(newValue),
                sqrt(add(sqr(fractionUncertainty1), sqr(fractionalUncertainty2))).getValue()
            )
        );
    }

    default N addUncertainty(N uncertainty1, N uncertainty2) {
        return uncertaintyContext(
            () -> sqrt(add(sqr(uncertainty1), sqr(uncertainty2))).getValue()
        );
    }

    N roundingUncertainty(N n);

    default N powerUncertainty(
        final N base,
        final N baseUncertainty,
        final N exponent,
        final N exponentUncertainty,
        final N result){
        //https://en.wikipedia.org/wiki/Propagation_of_uncertainty#Linear_combinations
        if (isZero(base) && isZero(baseUncertainty)) {
            return base;
        }
        final N fbase = max(base, roundingUncertainty(base));
        return uncertaintyContext(
            () -> multiply(
                abs(result),
                sqrt(
                    add(
                        sqr(divide(multiply(exponent, baseUncertainty), fbase).getValue()),
                        sqr(multiply(ln(fbase).getValue(), exponentUncertainty))
                    )
                ).getValue()
            )
        );
    }

    default <X> X uncertaintyContext(Supplier<X> supplier) {
        return supplier.get();
    }

}
