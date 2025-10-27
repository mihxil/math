package org.meeuw.math.numbers;

import org.meeuw.math.abstractalgebra.DivisibleGroupElement;

/**
 * Representation of an integer operand. Either for division, or for multiplication.
 * @since 0.19
 */
public interface Factor {
    Factor ONE = new  LongFactor(1);

      static Factor ofPow10(int exponent) {
        if (exponent == 0) {
            return Factor.ONE;
        }
        return new PowerFactor(10, exponent );
    }

    static Factor ofPow(int base, int exponent) {
        if (exponent == 0) {
            return ONE;
        }
        return new PowerFactor(base, exponent );
    }

    <E extends DivisibleGroupElement<E>> E apply(E operand);

    double apply(double operand);

}
