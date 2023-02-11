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

import lombok.Getter;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.DivisionByZeroException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <S> Self reference
 * @param <E> type of real and imaginary part
 */
public abstract class AbstractComplexNumber<
    S extends AbstractComplexNumber<S, E, ES>,
    E extends ScalarFieldElement<E>,
    ES extends ScalarField<E>
    >
    implements
    FieldElement<S>,
    WithScalarOperations<S, E>,
    Serializable {

    static final long serialVersionUID = 0L;

    @Getter
    final E real;

    @Getter
    final E imaginary;


    public AbstractComplexNumber(E real, E imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    abstract S _of(E real, E imaginary);

    @Override
    public S times(S multiplier) {
        return _of(
            this.real.times(multiplier.real).minus(this.imaginary.times(multiplier.imaginary)),
            this.real.times(multiplier.imaginary).plus(this.imaginary.times(multiplier.real)));
    }

    @Override
    public S times(E multiplier) {
        return _of(this.real.times(multiplier), this.imaginary.times(multiplier));
    }

    @Override
    public S dividedBy(E divisor) {
        return _of(
            this.real.dividedBy(divisor),
            this.imaginary.dividedBy(divisor)
        );
    }

    @Override
    public S reciprocal() {
        E denominator = this.real.sqr().plus(this.imaginary.sqr());
        if (denominator.isZero()) {
            throw new DivisionByZeroException("Denominator was 0");
        }
        return _of(
            this.real.dividedBy(denominator),
            this.imaginary.negation().dividedBy(denominator)
        );
    }

    @Override
    public S plus(S summand) {
        return _of(
            this.real.plus(summand.real),
            this.imaginary.plus(summand.imaginary)
        );
    }

    @Override
    public S negation() {
        return _of(
            this.real.negation(),
            this.imaginary.negation()
        );
    }

    @Override
    public S dividedBy(long divisor) {
        return _of(real.dividedBy(divisor), imaginary.dividedBy(divisor));
    }


    @Override
    public S times(long multiplier) {
        return _of(real.times(multiplier), imaginary.times(multiplier));
    }



    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractComplexNumber<?, ?, ?> that = (AbstractComplexNumber) o;
        return real.equals(that.real) && imaginary.equals(that.imaginary);
    }

    @Override
    public boolean eq(S o) {
        return real.eq(o.real) && imaginary.eq(o.imaginary);
    }

    @Override
    public int hashCode() {
        int result = real.hashCode();
        result = 31 * result + imaginary.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean hasReal = ! real.isZero();
        if (hasReal) {
            result.append(real);
        }
        if (!imaginary.isZero()) {
            if (hasReal) {
                result.append(' ');
            }
            if (imaginary.isNegative()) {
                result.append('-');
            } else {
                if (hasReal) {
                    result.append('+');
                }
            }
            if (hasReal) {
                result.append(' ');
            }
            E abs = imaginary.abs();
            if (! abs.isOne()) {
                result.append(abs);
            }
            result.append("i");
        }
        if (result.length() == 0) {
            result.append("0");
        }
        return result.toString();
    }

}
