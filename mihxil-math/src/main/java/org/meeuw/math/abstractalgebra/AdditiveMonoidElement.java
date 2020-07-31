package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveMonoidElement<F extends AdditiveMonoidElement<F>> extends AlgebraicElement<F> {

    AdditiveMonoid<F> structure();

    F plus(F summand);
}
