package org.meeuw.math;


import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainNumbersField<T extends UncertainNumber<T> & FieldElement<T>> implements Field<T> {

    final T zero;
    final T one;

    public UncertainNumbersField(T zero, T one) {
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

    @Override
    public Cardinality cardinality() {
        return zero.structure().cardinality();
    }
}
