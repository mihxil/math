package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<F extends MultiplicativeGroupElement<F, A>, A extends MultiplicativeGroup<F, A>> extends AlgebraicStructure<F> {

    F one();

    @Override
    default boolean supports(Operator operator) {
        return operator == Operator.MULTIPLICATION || operator == Operator.DIVISION;
    }

}
