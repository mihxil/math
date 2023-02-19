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
package org.meeuw.math.abstractalgebra.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;

/**
 * The cartesian multiplication of two other groups.
 * <p>
 * Note that this is tested in mihxil-algebra, because it's handy to have some other groups.
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public class ProductGroup<A extends GroupElement<A>, B extends GroupElement<B>>
    extends AbstractAlgebraicStructure<ProductElement<A, B>>
    implements Group<ProductElement<A, B>>, Streamable<ProductElement<A, B>> {

    private static final Map<Key, ProductGroup<?, ?>>  INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <A extends GroupElement<A>, B extends GroupElement<B>> ProductGroup<A, B> of(Group<A> groupA, Group<B> groupB) {
        return (ProductGroup<A, B>) ofGeneric(groupA, groupB);
    }

    public static ProductGroup<?, ?> ofGeneric(Group<?> groupA, Group<?> groupB) {
        return INSTANCES.computeIfAbsent(new Key(groupA, groupB), (k) -> new ProductGroup<>(groupA, groupB));
    }

    @EqualsAndHashCode
    private static class Key {
        final Group<?> groupA;
        final Group<?> groupB;

        private Key(Group<?> groupA, Group<?> groupB) {
            this.groupA = groupA;
            this.groupB = groupB;
        }
    }

    @Getter
    private final Group<A> groupA;

    @Getter
    private final Group<B> groupB;


    private final ProductElement<A, B> one;


    private ProductGroup(Group<A> groupA, Group<B> groupB) {
        this.groupA = groupA;
        this.groupB = groupB;
        one = new ProductElement<>(this, groupA.unity(), groupB.unity());
    }

    @Override
    public ProductElement<A, B> unity() {
        return one;
    }

    @Override
    public Cardinality getCardinality() {
        return groupA.getCardinality().times(groupB.getCardinality());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<ProductElement<A, B>> stream() {
        if (groupA instanceof Streamable<?> && groupB instanceof Streamable<?>) {
            return StreamUtils.cartesianStream(
                () -> ((Streamable<A>) groupA).stream(),
                () -> ((Streamable<B>) groupB).stream()).map(a -> new ProductElement<>(this, (A) a[0], (B) a[1]));

        } else {
            throw new NotStreamable(String.format("No streaming because %s and/or %s are not Streamable", groupA, groupB));
        }
    }

    @Override
    public String toString() {
        return groupA + "⨯" + groupB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductGroup<?, ?> that = (ProductGroup<?, ?>) o;

        if (!groupA.equals(that.groupA)) return false;
        return groupB.equals(that.groupB);
    }

    @Override
    public int hashCode() {
        int result = groupA.hashCode();
        result = 31 * result + groupB.hashCode();
        return result;
    }
}
