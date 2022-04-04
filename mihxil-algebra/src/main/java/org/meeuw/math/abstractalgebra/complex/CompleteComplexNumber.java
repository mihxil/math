package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <S> self reference
 * @param <E> type of real and imaginary parts
 */
public abstract class CompleteComplexNumber<
    S extends CompleteComplexNumber<S, E>,
    E extends CompleteScalarFieldElement<E>>
    extends AbstractComplexNumber<S, E>
    implements
    CompleteFieldElement<S>,
    MetricSpaceElement<S, E>,
    Serializable {

    static final long serialVersionUID = 0L;

    public CompleteComplexNumber(E real, E imaginary) {
        super(real, imaginary);
    }

    @Override
    public abstract CompleteComplexNumbers<S, E> getStructure();


    @Override
    public S sqrt() {
        if (imaginary.isZero()) {
            if (real.isPositive() || real.isZero()) {
                return _of(real.sqrt(), getStructure().getElementStructure().zero());
            } else {
                return _of(getStructure().getElementStructure().zero(), real.abs().sqrt());
            }
        }
        E abs = abs();
        return _of(
            (abs.plus(real).dividedBy(2)).sqrt(),
            (abs.minus(real).dividedBy(2)).sqrt().times(imaginary.signum())
        );
    }

    @Override
    public S sin() {
        return _of(
            real.sin().times(imaginary.cosh()),
            real.cos().times(imaginary.sinh())
        );
    }

    @Override
    public S cos() {
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh())
        );
    }

    @Override
    public S pow(S exponent) throws ReciprocalException {
        // (a + bi) ^ (c + di) = (a + bi)^ c (a + bi) ^ di
        // = (a + bi)^c (
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh())
        );
    }

    @Override
    public S exp() {
        E pref = real.exp();
        return _of(
            pref.times(imaginary.cos()),
            pref.times(imaginary.sin())
        );
    }

    /**
     * principal value logarithm
     */
    @Override
    public S ln() {
        return _of(
            abs().ln(),
            getStructure().atan2(imaginary, real)
        );
    }


    @Override
    public E distanceTo(S otherElement) {
        E norm = (
            (real.minus(otherElement.real)).sqr()
                .plus(
                    (imaginary.minus(otherElement.imaginary)).sqr()));
        return norm.sqrt();
    }

}