package org.meeuw.math.abstractalgebra.complex;

import lombok.extern.java.Log;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.*;

/**
 * The {@link Field} of {@link ComplexNumber}s, backed by {@link BigDecimalElement}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Log
@Example(CompleteField.class)
public class BigComplexNumbers extends AbstractComplexNumbers<BigComplexNumber, BigDecimalElement>
    implements
    CompleteField<BigComplexNumber>,
    MetricSpace<BigComplexNumber, BigDecimalElement> {

    public static final BigComplexNumbers INSTANCE = new BigComplexNumbers();

    private BigComplexNumbers() {
        super(BigComplexNumber.class, BigDecimalField.INSTANCE);
    }

    @Override
    BigComplexNumber of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    @Override
    public String toString() {
        return "ℂ";
    }
}
