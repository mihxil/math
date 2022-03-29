package org.meeuw.math.abstractalgebra;

import java.io.Serializable;

/**
 * The base interface for elements of algebraic structures.
 *
 * Every element in a algebraic structure has at least a reference to the {@link AlgebraicStructure} of wich it is an element
 * of. See {@link #getStructure()}.
 *
 * An algebraic element should basicly be unmodifiable, at least in the sense that it from the start on  should represent the same <em>value</em>.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The self type
 */
public interface AlgebraicElement<E extends AlgebraicElement<E>> extends Serializable {

    /**
     * @return the {@link AlgebraicStructure} associated with the object.
     */
    AlgebraicStructure<E> getStructure();

    /**
     * The unary operator which does nothing. Just returns the current element again.
     *
     * This exists for two reasons<ol>
     *   <li>it is the most simply unary operator possible, which can be available on any structure</li>
     *   <li>it also makes implementing some default methods nicer, because it does the casting of {@code this} to the current generic.</li>
     *   </ol>
     *
     *  @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#IDENTIFY
     */
    @SuppressWarnings("unchecked")
    default E self() {
        return (E) this;
    }

    /**
     * The equal operator. May be the same as {@link #equals(Object)}, but
     *
     * <ol>
     *    <li>its argument is always inside the algebra (contrary to {@link #equals(Object)}, which accepts _any_ object</li>
     *    <li>it may not be fully transitive (in case of uncertain values)</li>
     * </ol>
     * @see org.meeuw.math.operators.BasicComparisonOperator#EQ
     */
    default boolean eq(E other) {
        return equals(other);
    }

}
