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
public interface MultiplicativeGroupElement<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoidElement<E> {

    @Override
    MultiplicativeGroup<E> getStructure();

    /**
     * The multiplicative inverse
     */
    E reciprocal();

    /**
     * if multiplication and division is defined, then so is exponentiation to any integer power.
     *
     * This default implementation is doing exponentiation by squaring
     */
    @Override
    default E pow(int n) {
        if (n < 0) {
            n *= -1;
            return reciprocal().pow(n);
        }
        if (n == 0) {
            return getStructure().one();
        }
        return MultiplicativeMonoidElement.super.pow(n);
    }


    default E dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }

}
