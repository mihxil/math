package org.meeuw.math.numbers;

import jakarta.validation.constraints.PositiveOrZero;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.DivisibleGroupElement;

/**
 * Representation of an integer operand. Either for division, or for multiplication.
 * @since 0.19
 */
public record Factor(long factor, boolean divide) {
    public static Factor ONE = new  Factor(1);

    public Factor(@PositiveOrZero long factor) {
        this(factor, false);
    }

    public static Factor ofPow10(int exponent) {
        if (exponent == 0) {
            return ONE;
        }
        long f = IntegerUtils.pow10(Math.abs(exponent));

        return new Factor(f, exponent < 0);
    }

    public static Factor ofPow(int base, int exponent) {
        if (exponent == 0) {
            return ONE;
        }
        long f = IntegerUtils.positivePow(base, Math.abs(exponent));
        return new Factor(f, exponent < 0);
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
