package org.meeuw.math.abstractalgebra;

/**
 * A group where it is not defined whether the operation is addition or multiplication.
 *
 * @author Michiel Meeuwissen
 */
public interface GroupElement<E extends GroupElement<E>> extends
    AlgebraicElement<E> {
    @Override
    Group<E> getStructure();

    E operate(E operand);

    E inverse();

}
