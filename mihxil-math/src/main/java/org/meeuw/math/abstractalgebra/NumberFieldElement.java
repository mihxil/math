package org.meeuw.math.abstractalgebra;

import org.meeuw.math.numbers.SignedNumberElement;

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
    SignedNumberElement<E> {

    @Override
    NumberField<E> getStructure();

    /**
     * Checks if a certain other value is equal, but allow for some deviations (rounding errors).
     */
    default boolean equalsWithEpsilon(E other, E epsilon) {
        E lower = minus(epsilon);
        E upper = plus(epsilon);
        return lower.compareTo(other) <= 0 && upper.compareTo(other) >= 0;
    }

    /**
     * Returns an estimation of the current representation precision. E.g. if the field is backend by a {@link Double} then
     * there will a about 15 decimal places, so this value is about 10^15 times as small as the value itself.
     *
     * The default implementation return {@link #getStructure()}{@link Field#zero()}. So then it is assumed that the representation is <em>exact</em>
     */
    default E epsilon() {
        return this.getStructure().zero();
    }

    /**
     * Using  {@link #epsilon()}, checks whether an other value is 'equal', but consider rounding errors, which would not make the values actually different.
     */
    default boolean equalsWithEpsilon(E other) {
        return equalsWithEpsilon(other, getStructure().max(epsilon(), other.epsilon()));
    }

}
