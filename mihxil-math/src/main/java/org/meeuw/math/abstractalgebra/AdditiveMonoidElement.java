package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveMonoidElement<E extends AdditiveMonoidElement<E>> extends AdditiveSemiGroupElement<E> {

    AdditiveMonoid<E> getStructure();

}
