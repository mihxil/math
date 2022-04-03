package org.meeuw.math;

import java.util.*;
import java.util.function.Supplier;

import org.meeuw.math.operators.OperatorInterface;

import static java.util.Collections.unmodifiableNavigableSet;

/**
 * @since 0.8
 */
public class CollectionUtils {
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
                            V t = wrapped.get();
                            value = t;
                            initialized = true;
                            return t;
                        }
                    }
                    return null;
                }
                return value;
            }
        };
    }
}
