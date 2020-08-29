package org.meeuw.math.abstractalgebra.complex;

import java.util.Objects;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber<E extends NumberFieldElement<E>>
    implements FieldElement<ComplexNumber<E>> {

    private final E real;
    private final E imaginary;


    public static <E extends NumberFieldElement<E>> ComplexNumber<E> of(E r, E imaginairy) {
        return new ComplexNumber<>(r, imaginairy);
    }

    public ComplexNumber(E real, E imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }


    @Override
    public ComplexNumbers<E> getStructure() {
        return ComplexNumbers.of(real.getStructure());
    }

    @Override
    public ComplexNumber<E> times(ComplexNumber<E> multiplier) {
        return new ComplexNumber<>(
            this.real.times(multiplier.real).minus(this.imaginary.times(multiplier.imaginary)),
            this.real.times(multiplier.imaginary).plus(this.imaginary.times(multiplier.real)));
    }

    public ComplexNumber<E> times(E multiplier) {
        return new ComplexNumber<>(
            this.real.times(multiplier), this.imaginary.times(multiplier)
        );
    }

    @Override
    public ComplexNumber<E> reciprocal() {
        E denominator = this.real.sqr().plus(this.imaginary.sqr());
        return of(this.real.dividedBy(denominator), this.imaginary.negation().dividedBy(denominator));
    }


    @Override
    public ComplexNumber<E> plus(ComplexNumber<E> summand) {
        return new ComplexNumber<>(
            this.real.plus(summand.real),
            this.imaginary.plus(summand.imaginary)
        );
    }

    @Override
    public ComplexNumber<E> negation() {
        return new ComplexNumber<>(
            this.real.negation(),
            this.imaginary.negation()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexNumber<?> that = (ComplexNumber<?>) o;
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
