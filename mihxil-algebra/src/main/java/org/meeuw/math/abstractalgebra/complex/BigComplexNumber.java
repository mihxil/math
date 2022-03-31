package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.CompleteFieldElement;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class BigComplexNumber extends AbstractComplexNumber<BigComplexNumber, BigDecimalElement>
    implements
    CompleteFieldElement<BigComplexNumber>,
    MetricSpaceElement<BigComplexNumber, BigDecimalElement>,
    Serializable {

    static final long serialVersionUID = 0L;

    public BigComplexNumber(BigDecimalElement real, BigDecimalElement imaginary) {
        super(real, imaginary);
    }

    public static BigComplexNumber of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    public static BigComplexNumber of(BigDecimalElement real) {
        return new BigComplexNumber(real, BigComplexNumbers.INSTANCE.getElementStructure().zero());
    }


    @Override
    protected BigComplexNumber _of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    @Override
    public BigComplexNumbers getStructure() {
        return BigComplexNumbers.INSTANCE;
    }

    @Override
    public BigComplexNumber sqrt() {
        if (imaginary.isZero()) {
            if (real.isPositive() || real.isZero()) {
                return _of(real.sqrt(), getStructure().getElementStructure().zero());
            } else {
                return _of(getStructure().getElementStructure().zero(), real.abs().sqrt());
            }
        }
        BigDecimalElement abs = abs();
        return _of(
            (abs.plus(real).dividedBy(2)).sqrt(),
            (abs.minus(real).dividedBy(2)).sqrt().times(imaginary.signum())
        );
    }

    @Override
    public BigComplexNumber sin() {
        return _of(
            real.sin().times(imaginary.cosh()),
            real.cos().times(imaginary.sinh())
        );
    }

    @Override
    public BigComplexNumber cos() {
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh())
        );
    }

    @Override
    public BigComplexNumber pow(BigComplexNumber exponent) throws ReciprocalException {

        // (a + bi) ^ (c + di) = (a + bi)^ c (a + bi) ^ di
        // = (a + bi)^c (
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh())
        );
        /// todo

    }

    @Override
    public BigComplexNumber exp() {
        BigDecimalElement pref = real.exp();
        return _of(
            pref.times(imaginary.cos()),
            pref.times(imaginary.sin())
        );
    }

    /**
     * principal value logarithm
     */
    @Override
    public BigComplexNumber ln() {
        return _of(
            abs().ln(),
            BigDecimalField.INSTANCE.atan2(imaginary, real)
        );
    }


    @Override
    public BigDecimalElement distanceTo(BigComplexNumber otherElement) {
        BigDecimalElement norm = (
            (real.minus(otherElement.real)).sqr()
                .plus(
                    (imaginary.minus(otherElement.imaginary)).sqr()));
        return norm.sqrt();
    }

}
