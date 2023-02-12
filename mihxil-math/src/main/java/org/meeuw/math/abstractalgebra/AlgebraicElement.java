/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra;

import java.io.Serializable;
import java.util.*;
import org.meeuw.math.exceptions.NotASubGroup;

/**
 * The base interface for elements of algebraic structures.
 * <p>
 * Every element in a algebraic structure has at least a reference to the {@link AlgebraicStructure} of wich it is an element
 * of. See {@link #getStructure()}.
 * <p>
 * An algebraic element should basically be unmodifiable, at least in the sense that it from the start on  should represent the same <em>value</em>.
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
     * <p>
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
     * The 'loosely' equal operator. This may be the same as {@link Object#equals(Object)}, but
     * <ol>
     *    <li>its argument is always inside the algebra (contrary to {@link Object#equals(Object)}, which accepts <em>any</em> object</li>
     *    <li>it may not be fully transitive (in case of uncertain values)</li>
     *    <li>for uncertain values it is more consistent, because e.g. the inverse of the inverse will return an object that is 'loosely' equal to the original object</li>
     * </ol>
     * @see org.meeuw.math.operators.BasicComparisonOperator#EQ
     * @return true if the other object is equal to this one
     */
    default boolean eq(E other) {
        return equals(other);
    }

    /**
     * Returns a comparator consistent with {@link #eq(AlgebraicElement)} (rather than {@link Object#equals(Object)}).
     * i.e. Two objects compare to {@code 0} if they are {@link #eq}.
     * <p>
     * If they are not equal in this way, the given comparator will be {@link Comparator#naturalOrder()} (if the objects are {@link Comparable}),
     * otherwise {@link Object#hashCode()} will determine comparison.
     *
     * @since 0.10
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    static <E extends AlgebraicElement<E>> Comparator<E> eqComparator() {
        return (o1, o2) -> {
            if (o1 != null && o2 != null && o1.eq(o2)) {
                return 0;
            }
            if (o1 instanceof Comparable<?> && o2 instanceof Comparable<?>) {
                return Objects.compare((Comparable) o1, (Comparable) o2, Comparator.naturalOrder());
            }
            return Objects.compare(o1 == null ? null : o1.hashCode(), o2 == null ? null : o2.hashCode(), Comparator.naturalOrder());
        };

    }

    /**
     * Not equals. Just a shortcut for {@code ! eq}
     * @see #eq(AlgebraicElement)
     */
    default boolean neq(E other) {
        return ! eq(other);
    }

    /**
     * Casts the current element to an element of a parent group. It should support the ones returned by
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
