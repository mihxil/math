/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 *  See <a href="https://en.wikipedia.org/wiki/Propagation_of_uncertainty#Linear_combinations">propagation of uncertainty</a>

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
     * The uncertainty propagation for multiplication (and division).
     */
    default N multiplicationUncertainty(
        N newValue,
        N fractionUncertainty1,
        N fractionalUncertainty2) {
        return withUncertaintyContext(
            () -> multiply(
                abs(newValue),
                sqrt(
                    add(
                        sqr(fractionUncertainty1),
                        sqr(fractionalUncertainty2)
                    )
                ).getValue()
            )
        );
    }

    /**
     * The uncertainty propagation for addition (and subtraction)
     */
    default N additionUncertainty(N uncertainty1, N uncertainty2) {
        return withUncertaintyContext(
            () -> sqrt(
                add(sqr(uncertainty1), sqr(uncertainty2))
            ).getValue()
        );
    }

    N roundingUncertainty(N n);


    default N powerUncertainty(
        final N base,
        final N baseUncertainty,
        final N exponent,
        final N exponentUncertainty,
        final N result){
        //
        if (isZero(base) && isZero(baseUncertainty)) {
            return base;
        }
        final N fbase = max(base, roundingUncertainty(base));
        return withUncertaintyContext(
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

    default N lnUncertainty(
        final N argument,
        final N argumentUncertainty
    ) {
        return withUncertaintyContext(() -> divide(argumentUncertainty, abs(max(argument, roundingUncertainty(argument)))).getValue());
    }

    default N expUncertainty(
        final N argument,
        final N argumentUncertainty,
        final N result
    ) {
        return withUncertaintyContext(() -> multiply(abs(result),argumentUncertainty));
    }

    /**
     * Runs some code in a context for calculating uncertainties.
     * <p>
     * This currently makes only sense when using {@link BigDecimal}. Uncertainties themselves need not be very precise. Naturally the mentioned context is a {@link java.math.MathContext}
     */
    default <X> X withUncertaintyContext(Supplier<X> supplier) {
        return supplier.get();
    }

}
