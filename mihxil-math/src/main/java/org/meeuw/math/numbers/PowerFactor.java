package org.meeuw.math.numbers;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.DivisibleGroupElement;

/**
 * Representation of an integer operand. Either for division, or for multiplication.
 * @since 0.19
 */
public record PowerFactor(int base, int exponent) implements Factor {

    public <E extends DivisibleGroupElement<E>> E apply(E operand) {
        if (base == 10) {
            return operand.scaleByPowerOfTen(exponent);
        } else {
            long factor = IntegerUtils.positivePow(base, Math.abs(exponent));
            if (exponent < 0) {
                return operand.dividedBy(factor);
            } else {
                return operand.times(factor);
            }
        }
    }

    public double apply(double operand) {
        double factor = Math.pow(base, Math.abs(exponent));

        if (exponent < 0) {
            return operand / factor;
        } else {
            return operand * factor;
        }
    }

}
