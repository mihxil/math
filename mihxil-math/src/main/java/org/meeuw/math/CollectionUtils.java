/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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

import org.meeuw.math.operators.OperatorInterface;

import static java.util.Collections.unmodifiableNavigableSet;

/**
 * @since 0.8
 */
public class CollectionUtils {

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

    public static <V> Supplier<V> memoize(final Supplier<V> wrapped){
        return new Supplier<V>() {
            transient volatile boolean initialized;
            transient V value;

            @Override
            public V get() {
                if (!initialized) {
                    synchronized (this) {
                        if (!initialized) {
                            value = wrapped.get();
                            initialized = true;
                        }
                    }
                }
                return value;
            }
        };
    }
}
