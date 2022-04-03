package org.meeuw.math.abstractalgebra.complex;

import lombok.extern.java.Log;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.text.TextUtils;

/**
 * The {@link Field} of {@link ComplexNumber}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
@Example(CompleteField.class)
public class ComplexNumbers extends CompleteComplexNumbers<ComplexNumber, RealNumber> {

    public static final ComplexNumbers INSTANCE = new ComplexNumbers();

    private ComplexNumbers() {
        super(ComplexNumber.class, RealField.INSTANCE);
    }

    @Override
    ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public String toString() {
        return "ℂ" + TextUtils.subscript("p");
    }

    @Override
    RealNumber atan2(RealNumber imaginary, RealNumber real) {
        return RealField.INSTANCE.atan2(imaginary, real);
    }
}
