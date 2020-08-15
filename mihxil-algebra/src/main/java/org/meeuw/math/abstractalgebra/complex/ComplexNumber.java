package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber<E extends NumberFieldElement<E>> implements FieldElement<ComplexNumber<E>> {

    private final E real;
    private final E imaginairy;


    public static <E extends NumberFieldElement<E>> ComplexNumber<E> of(E r, E imaginairy) {
        return new ComplexNumber<>(r, imaginairy);
    }

    public ComplexNumber(E real, E imaginairy) {
        this.real = real;
        this.imaginairy = imaginairy;
    }

    @Override
    public ComplexNumbers<E> getStructure() {
        return new ComplexNumbers<E>(real.getStructure());
    }

    @Override
    public ComplexNumber<E> times(ComplexNumber<E> multiplier) {
        return new ComplexNumber<>(
            this.real.times(multiplier.real).minus(this.imaginairy.times(multiplier.imaginairy)),
            this.real.times(multiplier.imaginairy).plus(this.imaginairy.times(multiplier.real)));
    }

    public ComplexNumber<E> times(E multiplier) {
        return new ComplexNumber<>(
            this.real.times(multiplier), this.imaginairy.times(multiplier)
        );
    }

    @Override
    public ComplexNumber<E> reciprocal() {
        E denominator = this.real.sqr().plus(this.imaginairy.sqr());
        return of(this.real.dividedBy(denominator), this.imaginairy.negation().dividedBy(denominator));
    }


    @Override
    public ComplexNumber<E> plus(ComplexNumber<E> summand) {
        return new ComplexNumber<>(
            this.real.plus(summand.real),
            this.imaginairy.plus(summand.imaginairy)
        );
    }

    @Override
    public ComplexNumber<E> negation() {
        return new ComplexNumber<>(
            this.real.negation(),
            this.imaginairy.negation()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexNumber<?> that = (ComplexNumber<?>) o;

        if (real != null ? !real.equals(that.real) : that.real != null) return false;
        return imaginairy != null ? imaginairy.equals(that.imaginairy) : that.imaginairy == null;
    }

    @Override
    public int hashCode() {
        int result = real != null ? real.hashCode() : 0;
        result = 31 * result + (imaginairy != null ? imaginairy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!real.isZero()) {
            result.append(real.toString());
        }
        if (!imaginairy.isZero()) {
            if (result.length() > 0) {
                result.append(" + ");
            }
            if (!imaginairy.isOne()) {
                result.append(imaginairy.toString());
            }
            result.append("i");
        }
        if (result.length() == 0) {
            result.append("0");
        }
        return result.toString();
    }
}
