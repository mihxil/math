package org.meeuw.math.abstractalgebra;

import java.io.Serializable;
import java.util.Optional;

import org.meeuw.math.exceptions.NotASubGroup;

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
     * @return true if the other object is equal to this one
     */
    default boolean eq(E other) {
        return equals(other);
    }

    /**
     * Casts the current element to an alement of a parent group. It should support the ones returned by
     * {@link AlgebraicStructure#getSuperGroups()}.
     *
     * @return A filled optional if successful
     * @param clazz The class of the object to cast to
     * @param <F> The type of the class of the object to cast to
     */
    default <F extends AlgebraicElement<F>> Optional<F> castDirectly(Class<F> clazz) {
        return Optional.empty();
    }

    /**
     * Casts this element as element of an ancestor structure.
     *
     * @return A new algebraic element, which same value, but as a member of a super structure
     * @param clazz The class of the object to cast to
     * @param <F> The type of the class of the object to cast to
     * @throws NotASubGroup if not castable.
     */
    default <F extends AlgebraicElement<F>> F cast(Class<F> clazz) {
        Optional<F> directly = castDirectly(clazz);
        if (directly.isPresent()) {
            return directly.get();
        } else {
            for (AlgebraicStructure<?> c : getStructure().getSuperGroups()) {
                for (AlgebraicStructure<?> ic: c.getAncestorGroups()) {
                    if (ic.equals(getStructure())) {
                        throw new IllegalStateException("");
                    }
                    if (ic.getElementClass().equals(clazz)) {
                        return cast(c.getElementClass()).cast(clazz);
                    }
                }
            }
        }
        throw new NotASubGroup(this, clazz);
    }


}
