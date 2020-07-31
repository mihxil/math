package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoid<F extends MultiplicativeMonoidElement<F>> extends MultiplicativeSemiGroup<F> {


    /**
     * The multiplicative group by definition has an element that is 'one', the multiplicative identity element.
     */
    F one();


}
