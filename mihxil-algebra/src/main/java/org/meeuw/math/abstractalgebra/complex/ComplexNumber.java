package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber extends AbstractComplexNumber<ComplexNumber, RealNumber>
    implements
    FieldElement<ComplexNumber>,
    MetricSpaceElement<ComplexNumber, RealNumber>,
    Serializable {

    static final long serialVersionUID = 0L;

    public ComplexNumber(RealNumber real, RealNumber imaginary) {
        super(real, imaginary);
    }

    @Override
    protected ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public ComplexNumbers getStructure() {
        return ComplexNumbers.INSTANCE;
    }


    @Override
    public RealNumber distanceTo(ComplexNumber otherElement) {
        RealNumber norm = (
            (real.minus(otherElement.real)).sqr()
                .plus(
                    (imaginary.minus(otherElement.imaginary)).sqr()));
        return norm.sqrt();
    }
}
