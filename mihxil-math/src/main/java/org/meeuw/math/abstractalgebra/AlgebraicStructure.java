package org.meeuw.math.abstractalgebra;

import java.lang.reflect.Array;
import java.util.*;

import org.meeuw.math.*;

/**
 * The base interface of all algebraic structures.
 *
 * If defines what arithmetic {@link Operator}s are possible its elements
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of this structure
 */
public interface AlgebraicStructure<E extends AlgebraicElement<E>> {

    NavigableSet<ComparisonOperator> EQ_ONLY = Utils.navigableSet(ComparisonOperator.EQ);

    /**
     * Returns the {@link Operator}s that elements of this structure support.
     * @return the set of all supported binary operators in this algebraic structure
     */
    default NavigableSet<Operator> getSupportedOperators() {
        return Collections.emptyNavigableSet();
    }



    /**
     * Returns the {@link UnaryOperator}s that elements of this structure support.
     * @return the set of all supported unary operators in this algebraic structure
     */
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return Collections.emptyNavigableSet();
    }

    /**
     * Returns the {@link UnaryOperator}s that elements of this structure support.
     * @return the set of all supported unary operators in this algebraic structure
     */
    default NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return EQ_ONLY;
    }

    /**
     * @return the cardinality of the complete set of this structure.
     */
    Cardinality getCardinality();

     /**
     * @return the java class of the elements of this algebraic structure
     */
    Class<E> getElementClass();

    /**
     * @return a functional interface that can check whether two elements of this structure are equal.
     *
     * Default this simply returns {@link Objects#equals(Object, Object)}.
     */
    default Equivalence<E> getEquivalence() {
        return Objects::equals;
    }

    default String getDescription() {
        return getClass().getSimpleName();
    }


    default E[][] newMatrix(int i, int j) {
        return MatrixUtils.newMatrix(getElementClass(), i, j);
    }

    @SuppressWarnings("unchecked")
    default E[] newArray(int i) {
        return (E[]) Array.newInstance(getElementClass(), i);
    }

}
