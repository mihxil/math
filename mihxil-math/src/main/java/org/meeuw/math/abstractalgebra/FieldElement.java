package org.meeuw.math.abstractalgebra;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldElement<E extends FieldElement<E>> extends
    DivisionRingElement<E>,
    MultiplicativeGroupElement<E> {

    @Override
    Field<E> getStructure();

}
