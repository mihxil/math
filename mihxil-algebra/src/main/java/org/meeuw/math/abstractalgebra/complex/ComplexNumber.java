package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber extends CompleteComplexNumber<ComplexNumber, RealNumber, RealField> {

    static final long serialVersionUID = 0L;

    public ComplexNumber(RealNumber real, RealNumber imaginary) {
        super(real, imaginary);
    }

    public static ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    public static ComplexNumber of(RealNumber real) {
        return new ComplexNumber(real, ComplexNumbers.INSTANCE.getElementStructure().zero());
    }


    @Override
    protected ComplexNumber _of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public ComplexNumbers getStructure() {
        return ComplexNumbers.INSTANCE;
    }
}
