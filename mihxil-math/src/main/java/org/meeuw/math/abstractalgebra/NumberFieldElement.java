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



}
