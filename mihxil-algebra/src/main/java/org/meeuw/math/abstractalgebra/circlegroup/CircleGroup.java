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

import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotStreamable;

/**
 * The additive group of values on an interval with its endpoints identified.
 *
 * <p>Given a scalar field {@code S}, a lower bound {@code l}, and an upper bound {@code h},
 * this represents the quotient group {@code S / (h - l)Z}. Each {@link CircleElement} has a
 * canonical representative in the half-open interval {@code [l, h)}. Addition consequently
 * wraps around at {@code h}.</p>
 *
 * <p>For example, {@code CircleGroup.of(RationalNumber.of(360))} represents angles in degrees:
 * {@code 360} is equal to {@code 0}, and adding {@code 90} to {@code 315} produces
 * {@code 45}.</p>
 *
 * @author Michiel Meeuwissen
 * @since 0.21
 * @param <E> the type of scalar used for the representatives
 * @param <C> the completed scalar type associated with {@code E}
 */
public class CircleGroup<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>>
    extends AbstractAlgebraicStructure<CircleElement<E, C>>
    implements
    AdditiveAbelianGroup<CircleElement<E, C>>, Streamable<CircleElement<E, C>> {

    final ScalarField<E, C> field;
    final E  lower;
    final E  higher;
    final E width;

    private final CircleElement<E, C> zero;


    protected CircleGroup(ScalarField<E, C> field,
                        E  lower,
                        E  higher) {
        this.field = field;
        this.lower = lower;
        this.higher = higher;
        this.width = higher.minus(lower);
        this.zero = new CircleElement<>(this, this.lower);
    }

    /**
     * Creates a circle group with canonical representatives in {@code [0, width)}.
     *
     * @param width the strictly positive distance after which values wrap around
     * @param <E> the type of scalar used for the representatives
     * @param <C> the completed scalar type associated with {@code E}
     * @return the circle group with the supplied circumference
     */
    public static <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>>
    CircleGroup<E, C> of(E  width) {
        return new CircleGroup<>(width.getStructure(), width.getStructure().zero(), width);
    }

    public static <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>>
    CircleGroup<E, C> of(E lower, E  higher) {
        return new CircleGroup<>(lower.getStructure(), lower, higher);
    }


    public CircleElement<E, C> element(E  value) {
        return new CircleElement<>(this, value);
    }
    /**
     * Returns the identity element, represented by the lower bound of this group's interval.
     *
     * @return the zero element
     */
    @Override
    public CircleElement<E, C> zero() {
        return zero;
    }

    /**
     * Returns the elements when the underlying scalar field is enumerable.
     *
     * @return a stream of circle elements
     * @throws NotStreamable when the underlying scalar field cannot be streamed
     */
    @Override
    public Stream<CircleElement<E, C>> stream() {
        if (field instanceof Streamable<?> streamable) {
            return streamable.stream()
                .map(o -> (E) o)
                .filter(o -> o.compareTo(lower) >= 0 && o.compareTo(higher) < 0)
                .map(r -> new CircleElement<E, C>(this, r))

                ;
        } else {
            throw new NotStreamable(field + " is not streamable");
        }
    }

    E lower() {
        return lower;
    }
    E higher() {
        return higher;
    }

    /**
     * Indicates that addition on this circle group is commutative.
     *
     * @return {@code true}
     */
    public boolean additionIsCommutative() {
        return true;
    }

    /**
     * Returns the cardinality inherited from the underlying scalar field.
     *
     * @return the cardinality of this group
     */
    @Override
    public Cardinality getCardinality() {
        return field.getCardinality();
    }

    /**
     * Returns a randomly selected, canonically normalized circle element.
     *
     * @param random the source of randomness
     * @return a random circle element
     */
    @Override
    public CircleElement<E, C> nextRandom(Random random) {
        return new CircleElement<>(this, field.nextRandom(random));
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CircleGroup<?, ?> that)) return false;

        return Objects.equals(field, that.field) && Objects.equals(lower, that.lower) && higher.equals(that.higher);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(field);
        result = 31 * result + Objects.hashCode(lower);
        result = 31 * result + higher.hashCode();
        return result;
    }
}
