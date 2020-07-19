package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditionGroupElement<F extends AdditionGroupElement<F, A>, A extends AlgebraicStructure<F>> {

    A structure();

    F plus(F summand);

    default F minus(F subtrahend) {
        return plus(subtrahend.negate());
    }
    F negate();

}
