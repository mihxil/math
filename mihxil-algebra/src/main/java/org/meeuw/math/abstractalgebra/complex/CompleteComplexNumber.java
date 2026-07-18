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

import java.io.Serial;
import java.io.Serializable;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.IllegalLogarithmException;

import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.LN;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQRT;

/**
 * Implementation of the {@link CompleteFieldElement} methods for rational numbers.
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <S> self reference
 * @param <E> type of real and imaginary parts
 */
public abstract class CompleteComplexNumber<
    S extends CompleteComplexNumber<S, E, ES>,
    E extends CompleteScalarFieldElement<E>,
    ES extends CompleteScalarField<E>>
    extends AbstractComplexNumber<S, E, ES, E>
    implements
    CompleteFieldElement<S>,
    MetricSpaceElement<S, E>,
    Serializable {

    @Serial
    private static final long serialVersionUID = 0L;

    public CompleteComplexNumber(E real, E imaginary) {
        super(real, imaginary);
    }

    @Override
    public abstract @NonNull CompleteComplexNumbers<S, E, ES> getStructure();


    @Override
    public S sqrt() {
        if (imaginary.isZero()) {
            E z = getStructure().getElementStructure().zero();
            if (real.isPositive() || real.isZero()) {
                return _of(real.sqrt(), z);
            } else {
                return _of(z, real.abs().sqrt());
            }
        }
        E abs = abs();
        return _of(
            (abs.plus(real).dividedBy(2)).sqrt(),
            (abs.minus(real).dividedBy(2)).sqrt().times(imaginary.signum())
        );
    }

    @Override
    public S root(int i) {
        return pow(getStructure().one().dividedBy(i));
    }

    @Override
    public S sin() {
        return _of(
            real.sin().times(imaginary.cosh()),
            real.cos().times(imaginary.sinh())
        );
    }

    @Override
    public S asin() {
        var i = getStructure().i();
        var one = getStructure().one();
        return LN(
            this.x(i).p(SQRT(one.minus(this.sqr())))
        ).dividedBy(i);
    }

    @Override
    public S cos() {
        return _of(
            real.cos().times(imaginary.cosh()),
            real.sin().times(imaginary.sinh().negation())
        );
    }

    @Override
    public S acos() {
        var i = getStructure().i();
        var o = getStructure().one();
        return LN(
            this.plus(SQRT(this.sqr().minus(o)))
        ).times(i.negation());
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
    @NonAlgebraic(reason = NonAlgebraic.Reason.NON_ALL_ELEMENTS, value="Cannot take logarithm of zero")
    public S ln() throws IllegalLogarithmException {
        E abs = abs();
        return _of(
            LN(abs),
            getStructure().atan2(imaginary, real)
        );
    }

    /**
     * See <a href="https://arxiv.org/abs/2603.21852">All elementary functions from a single binary operator</a></a>
     * @param y
     * @since 0.20
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.NON_ALL_ELEMENTS, value="Cannot take logarithm of zero")
    public S eml(S y) {
        return exp().minus(y.ln());
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
