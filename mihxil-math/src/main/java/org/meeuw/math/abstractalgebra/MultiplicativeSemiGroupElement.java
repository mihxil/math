package org.meeuw.math.abstractalgebra;

import javax.validation.constraints.Min;

import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * Elements of a {@link MultiplicativeSemiGroup} can be multiplied by each other (via {@link #times(MultiplicativeSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupElement<E extends MultiplicativeSemiGroupElement<E>> extends AlgebraicElement<E> {

    @Override
    MultiplicativeSemiGroup<E> getStructure();

    E times(E multiplier);

    /**
     * less verbose version of {@link #times(MultiplicativeSemiGroupElement)}
     */
    default E x(E multiplier) {
        return times(multiplier);
    }
    /**
     * if multiplication is defined, then so is exponentiation, as long as the exponent is a positive integer.
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    default E pow(@Min(1) int n) {
        if (n < 0) {
            throw new ReciprocalException("Not defined for negative exponents");
        }
        if (n == 0) {
            throw new DivisionByZeroException("Not definied for exponent = 0");
        }
        E y = null;
        E x = (E) this;
        while (n > 1) {
            if (n % 2 == 1) {
                y = y == null ? x : x.times(y);
                n = (n - 1) / 2;
            } else {
                n /= 2;
            }
            x = x.times(x);
        }
        return y == null ? x : x.times(y);
    }

    /**
     * Returns this element multiplied by itself.
     */
    @SuppressWarnings("unchecked")
    default E sqr() {
        return times((E) this);
    }

}
