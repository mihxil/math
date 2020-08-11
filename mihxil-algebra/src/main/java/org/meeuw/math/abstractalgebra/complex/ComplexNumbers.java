package org.meeuw.math.abstractalgebra.complex;

import lombok.Getter;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumbers<E extends NumberFieldElement<E>> extends AbstractAlgebraicStructure<ComplexNumber<E>> implements Field<ComplexNumber<E>> {

    @Getter
    private final NumberField<E> elementStructure;

    public ComplexNumbers(NumberField<E> elementStructure) {
        super((Class) ComplexNumber.class);
        this.elementStructure = elementStructure;
    }

    @Override
    public ComplexNumber<E> zero() {
        return new ComplexNumber<>(this.elementStructure.zero(), this.elementStructure.zero());
    }

    @Override
    public ComplexNumber<E> one() {
        return new ComplexNumber<>(this.elementStructure.one(), this.elementStructure.zero());
    }

    public ComplexNumber<E> i() {
        return new ComplexNumber<>(this.elementStructure.zero(), this.elementStructure.one());
    }

    @Override
    public Cardinality getCardinality() {
        //return field.cardinality(); // might be doable...
        return Cardinality.ALEPH_1;
    }
}
