/*
 *  Copyright 2026 Michiel Meeuwissen
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
package org.meeuw.math.abstractalgebra.circlegroup;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.AlgebraicStructureException;

/**
 * An element of a {@link CircleGroup}, represented by a scalar position on its circumference.
 *
 * <p>The constructor normalizes every supplied value to the group's canonical half-open interval.
 * Consequently, values separated by a whole circumference are equal: in a group with width
 * {@code 1}, {@code 0}, {@code 1}, and {@code -1} denote the same element.</p>
 *
 * @param <E> the type of scalar used for the representative
 * @param <C> the completed scalar type associated with {@code E}
 * @since 0.21
 */
public class CircleElement<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>>
 implements
    AdditiveGroupElement<CircleElement<E, C>> {

    protected CircleGroup<E, C> group;
    protected E value;

    /**
     * Creates the element represented by {@code value}.
     *
     * @param group the owning circle group
     * @param value the value to normalize into the group's canonical interval
     */
    protected CircleElement(CircleGroup<E, C> group, E value) {
        this.group = group;
        this.value = value.minus(group.lower).mod(group.width).plus(group.lower);
    }

    /**
     * Returns the circle group to which this element belongs.
     *
     * @return this element's group
     */
    @Override
    public @NonNull CircleGroup<E, C> getStructure() {
        return group;
    }

    /**
     * Adds another element of this circle group, wrapping at the group's circumference.
     *
     * @param summand the element to add
     * @return the normalized sum
     */
    @Override
    public CircleElement<E, C> plus(CircleElement<E, C> summand) {
        if (! summand.group.eq(group)) {
            throw new AlgebraicStructureException("Cannot add " + summand + " to " + this);
        }
        return new CircleElement<>(group, this.value.plus(summand.value));
    }

    /**
     * Returns the additive inverse of this element on the circle.
     *
     * @return the normalized inverse
     */
    @Override
    public CircleElement<E, C> negation() {
        return new CircleElement<>(group, this.value.negation());
    }

    /**
     * Tests whether another circle element has the same group and canonical representative.
     *
     * @param other the element to compare with
     * @return whether the elements are equal
     */
    @Override
    public boolean eq(CircleElement<E, C> other) {
        return this.group.eq(other.group) && this.value.eq(other.value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CircleElement other) {
            return this.eq(((CircleElement<E, C>)other));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode() + 13 * group.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }


}
