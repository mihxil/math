package org.meeuw.math.abstractalgebra.polynomial;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.ArrayUtils;
import org.meeuw.math.abstractalgebra.*;

/**
 * @since 0.19
 */
public class PolynomialRing<E extends RingElement<E>>  extends AbstractAlgebraicStructure<Polynomial<E>> implements Ring<Polynomial<E>> {

    private static final Map<Ring<?>, Map<String, PolynomialRing<?>>> CACHE = new ConcurrentHashMap<>();
    @Getter
    private final Ring<E> coefficientRing;
    @Getter
    private final String variable;

    private PolynomialRing(Ring<E> coefficientRing, String variable) {
        this.coefficientRing = coefficientRing;
        this.variable = variable;
    }

    @SuppressWarnings("unchecked")
    public static <E extends RingElement<E>> PolynomialRing<E> of(Ring<E> coefficientRing, String variable) {
        return (PolynomialRing<E>) CACHE.computeIfAbsent(coefficientRing, (k) -> new HashMap<>())
            .computeIfAbsent(variable, k -> new PolynomialRing<>(coefficientRing, variable));
    }
    public static <E extends RingElement<E>> PolynomialRing<E> of(Ring<E> coefficientRing) {
        return of(coefficientRing, "x");
    }

    @Override
    public Polynomial<E> one() {
        return new Polynomial<>(this, coefficientRing.one());
    }

    @Override
    public Polynomial<E> zero() {
        return new Polynomial<>(this);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0.times(coefficientRing.getCardinality());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PolynomialRing<?> that)) return false;

        return Objects.equals(coefficientRing, that.coefficientRing) && Objects.equals(variable, that.variable);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(coefficientRing);
        result = 31 * result + Objects.hashCode(variable);
        return result;
    }

    @SafeVarargs
    public final Polynomial<E> newElement(E... coefficients) {
        E[] trimmed = ArrayUtils.removeTrailingIf(AdditiveMonoidElement::isZero, getCoefficientRing().getElementClass(), coefficients);
        return new Polynomial<>(this, trimmed);
    }



}
