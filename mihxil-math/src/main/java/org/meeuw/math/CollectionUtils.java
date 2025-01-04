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
package org.meeuw.math;

import java.util.*;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.functional.Suppliers;
import org.meeuw.math.operators.OperatorInterface;

import static java.util.Collections.unmodifiableNavigableSet;

/**
 * @since 0.8
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    @SafeVarargs
    public static <E> NavigableSet<E> navigableSet(Comparator<? super E> comparator, E...ops) {
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(Arrays.asList(ops));
        return unmodifiableNavigableSet(set);
    }

    @SafeVarargs
    public static <E extends OperatorInterface> NavigableSet<E> navigableSet(E...ops) {
        return navigableSet(OperatorInterface.COMPARATOR, ops);
    }

    @SafeVarargs
    public static <E> NavigableSet<E> navigableSet(NavigableSet<E> extend, E...ops) {
        TreeSet<E> base = new TreeSet<>(extend);
        base.addAll(Arrays.asList(ops));
        return unmodifiableNavigableSet(base);
    }

    @SafeVarargs
    public static <E> NavigableSet<E> navigableSet(Comparator<? super E> comparator, Collection<E>... extend) {
        TreeSet<E> base = new TreeSet<>(comparator);
        for (Collection<E> col : extend) {
            base.addAll(col);
        }
        return unmodifiableNavigableSet(base);
    }

    @SafeVarargs
    public static <E extends OperatorInterface> NavigableSet<E> navigableSet(Collection<E>... extend) {
        return navigableSet(OperatorInterface.COMPARATOR, extend);
    }

    /**
     * @deprecated Use {@link Suppliers#memoize(Supplier)}
     */
    @Deprecated
    public static <V> Supplier<V> memoize(final Supplier<V> wrapped) {
        return Suppliers.memoize(wrapped);
    }


    /**
     * Wraps the given set in a new set, with the same elements.
     * <p>
     * The only difference will be that its {@link Set#contains(Object)} will simply return {@code false} if the argument is {@code null}.
     * @since 0.12
     */
    public static <P> Set<@NonNull P> nullSafeSet(@NonNull final Set<@NonNull P> set) {
        return new AbstractSet<P>() {
            @Override
            public @NonNull Iterator<P> iterator() {
                return set.iterator();
            }

            @Override
            public int size() {
                return set.size();
            }

            @Override
            public boolean add(@NonNull P o) {
                return set.add(o);
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return o != null && set.contains(o);
            }
        };
    }

}
