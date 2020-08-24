package org.meeuw.math;

/**
 * @author Michiel Meeuwissen
 * @since 0.
 */
@FunctionalInterface
public interface Equivalence<E> {

    boolean test(E e1, E e2);
}
