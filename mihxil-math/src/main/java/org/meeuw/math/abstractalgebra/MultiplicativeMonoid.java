package org.meeuw.math.abstractalgebra;

/**
 * This algebraic structure defines also the multiplicative identify {@link #one()}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoid<F extends MultiplicativeMonoidElement<F>> extends MultiplicativeSemiGroup<F> {


    /**
     * The multiplicative group by definition has an element that is 'one', the multiplicative identity element.
     */
    F one();


}
