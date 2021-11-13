package org.meeuw.math.abstractalgebra;

/**
 * Like {@link java.util.function.BiPredicate}, but the difference is that this is not itself generic, but only its {@link #test(AlgebraicElement, AlgebraicElement)} method.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
@FunctionalInterface
public interface AlgebraicComparisonOperator {

    <E extends AlgebraicElement<E>>  boolean test(E arg1, E arg2);

}
