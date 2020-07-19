package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditionGroup<F extends MultiplicativeGroupElement<F, A>, A extends AdditionGroup<F, A>> extends AlgebraicStructure<F> {

    F zero();

    @Override
    default boolean supports(Operator operator) {
        return operator == Operator.ADDITION || operator == Operator.SUBTRACTION;

    }

}
