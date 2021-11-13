package org.meeuw.math.abstractalgebra;

/**
 * An element of a {@link Ring}, so something that can be added, subtracted and multiplied by each other.
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface AbelianRingElement<E extends AbelianRingElement<E>> extends RingElement<E> {

    @Override
    AbelianRing<E> getStructure();

}
