package org.meeuw.math.abstractalgebra.polynomial;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.ArrayUtils;
import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.exceptions.NotStreamable;

/**
 * @since 0.19
 */

public class PolynomialRing<E extends AbelianRingElement<E>>
    extends AbstractAlgebraicStructure<Polynomial<E>>
    implements
    AbelianRing<Polynomial<E>>,
    Streamable<Polynomial<E>> {
    private static final Map<AbelianRing<?>, Map<String, PolynomialRing<?>>> CACHE = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    public static <E extends AbelianRingElement<E>> PolynomialRing<E> of(AbelianRing<E> coefficientRing, String variable) {
        return (PolynomialRing<E>) CACHE.computeIfAbsent(coefficientRing, (k) -> new HashMap<>())
            .computeIfAbsent(variable, k -> new PolynomialRing<>(coefficientRing, variable));
    }
    public static <E extends AbelianRingElement<E>> PolynomialRing<E> of(AbelianRing<E> coefficientRing) {
        return of(coefficientRing, "x");
    }

    @Example(Ring.class)
    public static PolynomialRing<RationalNumber> RATIONAL_POLYNOMIALS = of(RationalNumbers.INSTANCE);

    @Example(Ring.class)
    public static PolynomialRing<IntegerElement> INTEGER_POLYNOMIALS = of(Integers.INSTANCE);

    @Getter
    private final AbelianRing<E> coefficientRing;
    @Getter
    private final String variable;

    private PolynomialRing(AbelianRing<E> coefficientRing, String variable) {
        this.coefficientRing = coefficientRing;
        this.variable = variable;
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
    public boolean multiplicationIsCommutative() {
        return coefficientRing.multiplicationIsCommutative();
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


    @Override
    public String toString() {
        return coefficientRing.toString() + "[" +  variable + "]";
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("The polynomial ring in %s over %s".formatted(variable, coefficientRing.toString()));
    }


    @Override
    public Polynomial<E> nextRandom(Random r) {
        int i = r.nextInt(5);
        E[] coefficients = coefficientRing.newArray(i);
        for (int j = 0; j < coefficients.length; j++) {
            if (r.nextBoolean()) {
                coefficients[j] = coefficientRing.nextRandom(r);
            } else {
                coefficients[j] = coefficientRing.zero();
            }
        }
        return newElement(coefficients);
    }


    @Override
    public Stream<Polynomial<E>> stream() {
        throw new NotStreamable("Not streamable");
    }
}
