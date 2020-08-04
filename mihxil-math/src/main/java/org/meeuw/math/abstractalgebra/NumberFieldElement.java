package org.meeuw.math.abstractalgebra;

/**
 * * A {@link FieldElement} that is is also a {@link Number} (which sadly is not an interface, so we can't extend it here).
 *
 * This means it has all conversion methods like {@link #intValue()}, {@link #doubleValue()}, and also for example that they are {@link Comparable}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberFieldElement<E extends NumberFieldElement<E>>   extends
    FieldElement<E>,
    NumberElement<E> {

    @Override
    NumberField<E> structure();

    default boolean equalsWithEpsilon(E other, E epsilon) {
        E lower = minus(epsilon);
        E upper = plus(epsilon);
        return lower.compareTo(other) <= 0 && upper.compareTo(other) >= 0;
    }
    default E epsilon() {
        return structure().zero();
    }
    default boolean equalsWithEpsilon(E other) {
        return equalsWithEpsilon(other, epsilon());
    }

}
