package org.meeuw.math.abstractalgebra.complex;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public class ComplexNumbers<E extends NumberFieldElement<E>> extends AbstractAlgebraicStructure<ComplexNumber<E>> implements Field<ComplexNumber<E>> {

    @Getter
    private final NumberField<E> elementStructure;

    private final ComplexNumber<E> zero;
    private final ComplexNumber<E> one;
    private final ComplexNumber<E> i;

    private static final Map<NumberField<?>, ComplexNumbers<?>> INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <E extends NumberFieldElement<E>> ComplexNumbers<E> of(NumberField<E> numberFieldElement) {
        return (ComplexNumbers<E>) INSTANCES.computeIfAbsent(numberFieldElement, k -> {
            log.info("Created new instance for " + k);
            return new ComplexNumbers<>(k);
            }

        );
    }

    private ComplexNumbers(NumberField<E> elementStructure) {
        super((Class) ComplexNumber.class);
        this.elementStructure = elementStructure;
        this.zero = new ComplexNumber<>(this.elementStructure.zero(), this.elementStructure.zero());
        this.one  = new ComplexNumber<>(this.elementStructure.one(), this.elementStructure.zero());
        this.i =  new ComplexNumber<>(this.elementStructure.zero(), this.elementStructure.one());
    }

    @Override
    public ComplexNumber<E> zero() {
        return zero;
    }

    @Override
    public ComplexNumber<E> one() {
        return one;
    }

    public ComplexNumber<E> i() {
        return i;
    }

    @Override
    public Cardinality getCardinality() {
        //return field.cardinality(); // might be doable...
        return Cardinality.ALEPH_1;
    }
}
