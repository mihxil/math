package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupElement<F extends AdditiveGroupElement<F>> extends AlgebraicElement<F> {

    @Override
    AdditiveGroup<F> structure();

    F plus(F summand);

     /**
     * The additive inverse
     */
    F negation();

    default F minus(F subtrahend) {
        return plus(subtrahend.negation());
    }

    /**
     * If addition is defined, then you can also have 'repeated' addition. This is a bit, but not quite like {@link MultiplicativeGroupElement#times(MultiplicativeGroupElement)}
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
