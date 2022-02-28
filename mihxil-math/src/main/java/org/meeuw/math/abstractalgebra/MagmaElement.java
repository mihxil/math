package org.meeuw.math.abstractalgebra;

/**
 * A group where it is not defined whether the operation is addition or multiplication.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface MagmaElement<E extends MagmaElement<E>> extends
    AlgebraicElement<E> {

    @Override
    Magma<E> getStructure();

    E operate(E operand);

}
