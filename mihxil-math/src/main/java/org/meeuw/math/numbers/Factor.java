package org.meeuw.math.numbers;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.DivisibleGroupElement;

/**
 * Representation of an integer operand. Either for division, or for pult
 * @since 0.19
 */
public record Factor(long factor, boolean divide) {
    public static Factor ONE = new  Factor(1);


    public Factor(long factor) {
        this(factor, false);
    }


    public static Factor ofPow10(int exponent) {
        if (exponent == 0) {
            return ONE;
        }
        long f = IntegerUtils.pow10(Math.abs(exponent));

        return new Factor(Math.abs(f), exponent < 0);
    }

    public static Factor ofPow(int base, int exponent) {
        if (exponent == 0) {
            return ONE;
        }
        long f = IntegerUtils.positivePow(base, Math.abs(exponent));
        return new Factor(Math.abs(f), exponent < 0);
    }

    public <E extends DivisibleGroupElement<E>> E apply(E operand) {
        if (factor == 1) {
            return operand;
        }
        if (divide) {
            return operand.dividedBy(factor);
        } else {
            return operand.times(factor);
        }

    }
}
