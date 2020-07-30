package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidElement<F extends MultiplicativeMonoidElement<F>> extends MultiplicativeSemiGroupElement<F> {

	MultiplicativeMonoid<F> structure();

	F times(F summand);
}
