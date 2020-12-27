package org.meeuw.math;

import java.util.function.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@FunctionalInterface
public interface Equivalence<E> extends BiPredicate<E, E> {

    @Override
    boolean test(E e1, E e2);


}
