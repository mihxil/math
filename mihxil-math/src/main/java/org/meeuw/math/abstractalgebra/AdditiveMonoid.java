package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveMonoid<E extends AdditiveMonoidElement<E>> extends AdditiveSemiGroup<E> {

    /**
     * The additive group by definition has an element that is 'zero',  the additive identity element.
     * @return the additive identify element 'zero' for this monoid
     */
    E zero();

}
