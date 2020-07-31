package org.meeuw.math;


import org.meeuw.math.abstractalgebra.NumberField;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainNumbers<T extends UncertainNumber<T>> implements NumberField<T> {

    final T zero;
    final T one;

    public UncertainNumbers(T zero, T one) {
        this.zero = zero;
        this.one = one;
    }

    @Override
    public T zero() {
        return zero;
    }

    @Override
    public T one() {
        return one;
    }
}
