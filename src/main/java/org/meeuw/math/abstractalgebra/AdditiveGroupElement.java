package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupElement<F extends AdditiveGroupElement<F, A>, A extends AlgebraicStructure<F, A>> extends AlgebraicElement<F, A> {

    F plus(F summand);

    default F minus(F subtrahend) {
        return plus(subtrahend.negation());
    }

     /**
     * The additive inverse
     */
    F negation();

}
