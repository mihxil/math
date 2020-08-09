package org.meeuw.math.uncertainnumbers;


import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainNumbersField<T extends UncertainNumber<T> & FieldElement<T>>
    extends AbstractAlgebraicStructure<T>
    implements Field<T> {

    final T zero;
    final T one;

    public UncertainNumbersField(T zero, T one) {
        super((Class<T>) zero.getClass());
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
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
