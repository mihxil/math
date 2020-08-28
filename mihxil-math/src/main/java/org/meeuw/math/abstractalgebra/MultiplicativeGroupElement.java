package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * The operations {@link #reciprocal()}, {@link #dividedBy(MultiplicativeGroupElement)} and {@link #pow(int)} are on default implemented
 * using one of the others.
 *
 * You have to override at least one, to break the otherwise stack overflow.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupElement<E extends MultiplicativeGroupElement<E>> extends MultiplicativeMonoidElement<E> {

    @Override
    MultiplicativeGroup<E> getStructure();

    /**
     * The multiplicative inverse
     */
    E reciprocal();

    /**
     * if multiplication and division is defined, then so is exponentation to any integer power.
     */
    default E pow(int exponent) {
        E result = getStructure().one();
        E self = (E) this;
        while (exponent > 0) {
            result = result.times(self);
            exponent--;
        }
        while(exponent < 0) {
            result = result.dividedBy(self);
            exponent++;
        }
        return result;
    }


    default E dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }

    /**
     * Returns this elemented multiplied by itself.
     */
    default E sqr() {
        return times((E) this);
    }


}
