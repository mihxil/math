package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.CompleteFieldElement;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber extends AbstractComplexNumber<ComplexNumber, RealNumber>
    implements
    CompleteFieldElement<ComplexNumber>,
    MetricSpaceElement<ComplexNumber, RealNumber>,
    Serializable {

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

    @Override
    public ComplexNumber sqrt() {
        if (imaginary.isZero()) {
            if (real.isPositive() || real.isZero()) {
                return _of(real.sqrt(), getStructure().getElementStructure().zero());
            } else {
                return _of(getStructure().getElementStructure().zero(), real.abs().sqrt());
            }
        }
        RealNumber abs = abs();
        return _of(
            (abs.plus(real).dividedBy(2)).sqrt(),
            (abs.minus(real).dividedBy(2)).sqrt().times(imaginary.signum())
        );
    }

    @Override
    public ComplexNumber sin() {
        return _of(
            real.sin().times(imaginary.cosh()),
            real.cos().times(imaginary.sinh())
        );
    }

    @Override
    public ComplexNumber cos() {
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh())
        );
    }

    @Override
    public ComplexNumber pow(ComplexNumber exponent) throws ReciprocalException {

        // (a + bi) ^ (c + di) = (a + bi)^ c (a + bi) ^ di
        // = (a + bi)^c (
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh())
        );
        /// todo

    }

    @Override
    public ComplexNumber exp() {
        RealNumber pref = real.exp();
        return _of(
            pref.times(imaginary.cos()),
            pref.times(imaginary.sin())
        );
    }

    /**
     * principal value logarithm
     */
    @Override
    public ComplexNumber ln() {
        return _of(
            abs().ln(),
            RealField.INSTANCE.atan2(imaginary, real)
        );
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
