package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupElement<E extends AdditiveGroupElement<E>> extends AdditiveMonoidElement<E> {

    @Override
    AdditiveGroup<E> getStructure();

     /**
     * The additive inverse
     */
    E negation();

    default E minus(E subtrahend) {
        return plus(subtrahend.negation());
    }

    /**
     * If addition is defined, then you can also have 'repeated' addition. This is a bit, but not quite like {@link MultiplicativeGroupElement#times(MultiplicableElement)}}
     *
     * It's actually also more or less similar to {@link MultiplicativeGroupElement#pow(int)}
     */
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
