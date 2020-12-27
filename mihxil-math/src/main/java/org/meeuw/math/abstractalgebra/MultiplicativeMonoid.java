package org.meeuw.math.abstractalgebra;

/**
 * This {@link MultiplicativeSemiGroup} defines also the multiplicative identify {@link #one()}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoid<E extends MultiplicativeMonoidElement<E>> extends MultiplicativeSemiGroup<E> {

    /**
     * The multiplicative group by definition has an element that is 'one', the multiplicative identity element.
     */
    E one();

}
