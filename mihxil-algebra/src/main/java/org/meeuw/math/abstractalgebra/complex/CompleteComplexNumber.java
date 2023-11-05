/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.IllegalLogarithmException;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <S> self reference
 * @param <E> type of real and imaginary parts
 */
public abstract class CompleteComplexNumber<
    S extends CompleteComplexNumber<S, E, ES>,
    E extends CompleteScalarFieldElement<E>,
    ES extends CompleteScalarField<E>>
    extends AbstractComplexNumber<S, E, ES>
    implements
    CompleteFieldElement<S>,
    MetricSpaceElement<S, E>,
    Serializable {

    private static final long serialVersionUID = 0L;

    public CompleteComplexNumber(E real, E imaginary) {
        super(real, imaginary);
    }

    @Override
    public abstract CompleteComplexNumbers<S, E, ES> getStructure();


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
    public S exp() {
        E pref = real.exp();
        return _of(
            pref.times(imaginary.cos()),
            pref.times(imaginary.sin())
        );
    }

    /**
     * Principal value logarithm
     */
    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME, value="Cannot take logarithm of zero")
    public S ln() throws IllegalLogarithmException {
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
