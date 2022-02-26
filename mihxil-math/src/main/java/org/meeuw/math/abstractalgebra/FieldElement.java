package org.meeuw.math.abstractalgebra;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldElement<E extends FieldElement<E>> extends
    DivisionRingElement<E>,
    AbelianRingElement<E>,
    DivisibleGroupElement<E>,
    GroupElement<E> {

    @Override
    Field<E> getStructure();

    @Override
    default E operate(E operand) {
        return getStructure().groupOperator().apply(self(), operand);
    }

    @Override
    default E inverse() {
        return getStructure().groupOperator().inverse(self());
    }

}
