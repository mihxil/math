package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupElement<F extends AdditiveGroupElement<F>> extends AdditiveMonoidElement<F> {

    @Override
    AdditiveGroup<F> structure();

     /**
     * The additive inverse
     */
    F negation();

    default boolean isNegative() {
        return compareTo(structure().zero()) < 0;
    }

    default boolean isPositive() {
        return compareTo(structure().zero()) > 0;
    }

    default boolean isZero() {
        return compareTo(structure().zero()) == 0;
    }

    int compareTo(F compare);

    default F minus(F subtrahend) {
        return plus(subtrahend.negation());
    }

    /**
     * If addition is defined, then you can also have 'repeated' addition. This is a bit, but not quite like {@link MultiplicativeGroupElement#times(MultiplicableElement)}}
     *
     * It's actually also more or less similar to {@link MultiplicativeGroupElement#pow(int)}
     */
    default F repeatedPlus(int multiplier) {
        if (multiplier == 0) {
            return structure().zero();
        }
        int m = Math.abs(multiplier);
        F result = self();
        while (--m > 0) {
            result = result.plus(self());
        }
        if (multiplier < 0) {
            return result.negation();
        } else {
            return result;
        }
    }


}
