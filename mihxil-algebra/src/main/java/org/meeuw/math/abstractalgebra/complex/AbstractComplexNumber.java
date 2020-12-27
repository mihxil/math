package org.meeuw.math.abstractalgebra.complex;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AbstractComplexNumber<S extends AbstractComplexNumber<S, E>, E extends ScalarFieldElement<E>>
    implements
    FieldElement<S>,
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


    abstract S of(E real, E imaginary);

    @Override
    public S times(S multiplier) {
        return of(
            this.real.times(multiplier.real).minus(this.imaginary.times(multiplier.imaginary)),
            this.real.times(multiplier.imaginary).plus(this.imaginary.times(multiplier.real)));
    }

    public S times(E multiplier) {
        return of(this.real.times(multiplier), this.imaginary.times(multiplier));
    }

    @Override
    public S reciprocal() {
        E denominator = this.real.sqr().plus(this.imaginary.sqr());
        return of(
            this.real.dividedBy(denominator),
            this.imaginary.negation().dividedBy(denominator)
        );
    }

    @Override
    public S plus(S summand) {
        return of(
            this.real.plus(summand.real),
            this.imaginary.plus(summand.imaginary)
        );
    }

    @Override
    public S negation() {
        return of(
            this.real.negation(),
            this.imaginary.negation()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractComplexNumber<?, ?> that = (AbstractComplexNumber) o;
        return Objects.equals(real, that.real) && Objects.equals(imaginary, that.imaginary);
    }

    @Override
    public int hashCode() {
        int result = real != null ? real.hashCode() : 0;
        result = 31 * result + (imaginary != null ? imaginary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean hasReal = ! real.isZero();
        if (hasReal) {
            result.append(real.toString());
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
                result.append(abs.toString());
            }
            result.append("i");
        }
        if (result.length() == 0) {
            result.append("0");
        }
        return result.toString();
    }

}
