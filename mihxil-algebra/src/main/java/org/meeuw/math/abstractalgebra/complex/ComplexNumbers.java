package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumbers<E extends NumberFieldElement<E>> implements Field<ComplexNumber<E>> {

    private final NumberField<E> field;

    public ComplexNumbers(NumberField<E> field) {
        this.field = field;
    }

    @Override
    public ComplexNumber<E> zero() {
        return new ComplexNumber<>(this.field.zero(), this.field.zero());
    }

    @Override
    public ComplexNumber<E> one() {
        return new ComplexNumber<>(this.field.one(), this.field.zero());
    }

    public ComplexNumber<E> i() {
        return new ComplexNumber<>(this.field.zero(), this.field.one());
    }

    @Override
    public Cardinality cardinality() {
        //return field.cardinality(); // might be doable...
        return Cardinality.ALEPH_1;
    }
}
