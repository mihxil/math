package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupElement<E extends AdditiveGroupElement<E>>
    extends AdditiveMonoidElement<E>, GroupElement<E> {

    @Override
    AdditiveGroup<E> getStructure();

    /**
     * @return the additive inverse of this element
     */
    E negation();

    default E minus(E subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    default E inverse() {
        return negation();
    }

    /**
     * If addition is defined, then you can also have 'repeated' addition. This is a bit, but not quite like {@link MultiplicativeGroupElement#times(MultiplicativeSemiGroupElement)}
     *
     * It's actually also more or less similar to {@link MultiplicativeGroupElement#pow(int)}
     * @param multiplier the number of times this element is to be added to itself
     * @return this * multiplier
     */
    @SuppressWarnings("unchecked")
    default E repeatedPlus(int multiplier) {
        if (multiplier == 0) {
            return getStructure().zero();
        }
        int m = Math.abs(multiplier);
        E self = (E) this;
        E result = self;
        while (--m > 0) {
            result = result.plus(self);
        }
        if (multiplier < 0) {
            return result.negation();
        } else {
            return result;
        }
    }

}
