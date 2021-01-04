package org.meeuw.math.abstractalgebra.complex;

import lombok.extern.java.Log;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * The {@link Field} of {@link ComplexNumber}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public class ComplexNumbers extends AbstractComplexNumbers<ComplexNumber, RealNumber>
    implements
    Field<ComplexNumber>,
    MetricSpace<ComplexNumber, RealNumber> {

    public static ComplexNumbers INSTANCE = new ComplexNumbers();


    private ComplexNumbers() {
        super(ComplexNumber.class, RealField.INSTANCE);
    }

    @Override
    ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public String toString() {
        return "ℂ";
    }
}
