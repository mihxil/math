package org.meeuw.math.abstractalgebra.polynomial;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.text.TextUtils;

/**
 * @since 0.19
 */
public class Polynomial<E extends RingElement<E>> implements RingElement<Polynomial<E>> {

    private final E[] coefficients;

    private final PolynomialRing<E> ring;

    @SafeVarargs
    Polynomial(PolynomialRing<E> ring, E... coefficients) {
        this.ring = ring;
        this.coefficients = coefficients;
    }

    @Override
    public @NonNull PolynomialRing<E> getStructure() {
        return ring;
    }

    @Override
    public Polynomial<E> times(Polynomial<E> multiplier) {
        if (isZero() || multiplier.isZero()) {
            return ring.zero();
        }
        if (isOne()) {
            return multiplier;
        }
        if (multiplier.isOne()) {
            return this;
        }
        int degreeA = this.coefficients.length;
        int degreeB = multiplier.coefficients.length;
        @SuppressWarnings("unchecked")
        E[] result = (E[]) Array.newInstance(
            this.ring.getCoefficientRing().getElementClass(),
            degreeA + degreeB - 1
        );
        // Initialize result coefficients to zero
        for (int i = 0; i < result.length; i++) {
            result[i] = this.ring.getCoefficientRing().zero();
        }
        for (int i = 0; i < degreeA; i++) {
            for (int j = 0; j < degreeB; j++) {
                result[i + j] = result[i + j].plus(
                    this.coefficients[i].times(multiplier.coefficients[j])
                );
            }
        }
        return new Polynomial<>(ring, result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Polynomial<E> plus(Polynomial<E> summand) {
        E[] result = (E[]) Array.newInstance(this.ring.getCoefficientRing().getElementClass(), Math.max(this.coefficients.length, summand.coefficients.length));
        for (int i = 0; i < result.length; i++) {
            if (coefficients.length <= i) {
               result[i] = summand.coefficients[i];
            } else if (summand.coefficients.length <= i) {
                result[i] = coefficients[i];
            } else {
                result[i] = coefficients[i].plus(summand.coefficients[i]);
            }
        }
        Class<E> eClass = this.ring.getCoefficientRing().getElementClass();
        E[] trimmed = ArrayUtils.removeTrailingIf(AdditiveMonoidElement::isZero, eClass,  result);
        return new Polynomial<>(ring, trimmed);
    }

    @SuppressWarnings("unchecked")
    public Polynomial<E> derivative() {
        E[] derivative = (E[]) Array.newInstance(ring.getCoefficientRing().getElementClass(), Math.max(0, coefficients.length - 1));
        for (int i = 1; i < coefficients.length; i++) {
            derivative[i - 1] = coefficients[i].repeatedPlus(i);
        }
        return new Polynomial<>(ring, derivative);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Polynomial<E> negation() {
        E[] result = (E[]) Array.newInstance(this.ring.getCoefficientRing().getElementClass(), this.coefficients.length);
        for (int i = 0; i < coefficients.length; i++) {
            result[i] = coefficients[i].negation();
        }
        return new Polynomial<>(ring, result);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            if (!coefficients[i].isZero()) {
                if (!b.isEmpty()) {
                    b.append(" + ");
                }

                if (i != 0) {
                    if (!coefficients[i].isOne()) {
                        b.append(coefficients[i]);
                    }
                    b.append(TextUtils.TIMES);
                    b.append(ring.getVariable());
                    if (i != 1) {
                        b.append(TextUtils.superscript(i));
                    }
                } else {
                    b.append(coefficients[i]);

                }
            }
        }
        return b.toString();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Polynomial<?> that)) return false;

        return Arrays.equals(coefficients, that.coefficients) && Objects.equals(ring, that.ring);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(coefficients);
        result = 31 * result + Objects.hashCode(ring);
        return result;
    }
}
