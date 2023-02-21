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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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

public class ProductGroup
    extends AbstractAlgebraicStructure<ProductElement>
    implements Group<ProductElement>, Streamable<ProductElement> {

    private static final Map<List<Group<?>>, ProductGroup> INSTANCES = new ConcurrentHashMap<>();

    public static ProductGroup of(Group<?>... groups) {
        return INSTANCES.computeIfAbsent(asList(groups), ProductGroup::new);
    }

    private final ProductElement one;

    private final List<Group<?>> groups;

    private static List<Group<?>> asList(Group<?>... elements) {
        List<Group<?>> result = new ArrayList<>();
        for (Group<?> e : elements) {
            if (e instanceof ProductGroup) {
                result.addAll(((ProductGroup) e).groups);
            } else {
                result.add(e);
            }
        }
        return result;
    }

    private ProductGroup(List<Group<?>> groups) {
        this.groups = groups;
        one = ProductElement.withGroup(this,
            groups.stream()
                .map(Group::unity)
                .collect(Collectors.toList()));
    }

    @Override
    public ProductElement unity() {
        return one;
    }

    @Override
    public ProductElement nextRandom(Random random) {
        return ProductElement.withGroup(this,
            groups.stream()
                .map(g -> g.nextRandom(random))
                .collect(Collectors.toList()));
    }

    @Override
    public Cardinality getCardinality() {
        return groups.stream()
            .map(AlgebraicStructure::getCardinality)
            .reduce(Cardinality.ONE, Cardinality::times);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<ProductElement> stream() {
        boolean streamable = groups.stream()
            .allMatch(g -> g instanceof Streamable<?>);

        if (streamable) {
            List<Supplier<Stream<? extends GroupElement<?>>>> suppliers =
                groups.stream()
                    .map(g  -> (Supplier<Stream<? extends GroupElement<?>>>) () -> ((Streamable<GroupElement<?>>) g).stream())
                    .collect(Collectors.toList());
            Stream<GroupElement<?>[]> stream = StreamUtils.cartesianStream(suppliers);
            return stream.map(ProductElement::of);
        } else {
            throw new NotStreamable(String.format("No streaming because not all %s are Streamable", groups));
        }
    }

    @Override
    public String toString() {
        return groups.stream()
            .map(Object::toString)
            .collect(Collectors.joining("⨯"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductGroup that = (ProductGroup) o;

        return groups.equals(that.groups);
    }

}
