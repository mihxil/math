package org.meeuw.math.abstractalgebra;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRingElement<E extends DivisionRingElement<E>> extends
    MultiplicativeGroupElement<E>,
    RingElement<E> {

    @Override
    DivisionRing<E> getStructure();
}
