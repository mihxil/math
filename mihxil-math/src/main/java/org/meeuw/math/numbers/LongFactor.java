package org.meeuw.math.numbers;

import jakarta.validation.constraints.PositiveOrZero;

import org.meeuw.math.abstractalgebra.DivisibleGroupElement;

/**
 * Representation of an integer operand. Either for division, or for multiplication.
 * @since 0.19
 */
public record LongFactor(long factor, boolean divide) implements Factor {

    public LongFactor(@PositiveOrZero long factor) {
        this(factor, false);
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
    public double apply(double operand) {
        if (factor == 1) {
            return operand;
        }
        if (divide) {
            return operand / factor;
        } else {
            return operand * factor;
        }
    }


}
