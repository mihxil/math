package org.meeuw.math.abstractalgebra.polynomial;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.meeuw.math.ArrayUtils;
import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;
import static org.meeuw.math.CollectionUtils.navigableSet;

import org.meeuw.math.text.TextUtils;

/**
 * The algebraic structures for polynomials (in one variable).
 * <p>
 *  For every variable, for every {@link AbelianRing} a polynomial ring object can be {@link #of(AbelianRing, String) acquired}.
 *  After that its elements, the {@link Polynomial polynomials} itself, can be instantiated with {@link #newElement(AbelianRingElement[])}
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

    /**
     * Defaulting version of {@link #of(AbelianRing, String)}, where the variable is 'x'.
     */
    public static <E extends AbelianRingElement<E>> PolynomialRing<E> of(AbelianRing<E> coefficientRing) {
        return of(coefficientRing, "x");
    }

    @Example(AbelianRing.class)
    public static final PolynomialRing<RationalNumber> RATIONAL_POLYNOMIALS = of(RationalNumbers.INSTANCE);

    @Example(AbelianRing.class)
    public static final PolynomialRing<IntegerElement> INTEGER_POLYNOMIALS = of(Integers.INSTANCE);

    private static final AlgebraicUnaryOperator DERIVATIVE = new AlgebraicUnaryOperator() {
            final Method method = getDeclaredMethod(Polynomial.class, "derivative");

            @Override
            @SneakyThrows
            public <E extends AlgebraicElement<E>> E apply(E e) {
                try {
                    return (E) method.invoke(e);
                } catch (Exception ex) {
                    throw ex.getCause();
                }
            }

            @Override
            public String stringify(String element) {
                return element + "'";
            }

            @Override
            public String name() {
                return "derivative";
            }
    };

    private static final NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(
        Rng.UNARY_OPERATORS,
        DERIVATIVE
    );
    @Getter
    private final AbelianRing<E> coefficientRing;
    @Getter
    private final String variable;

    private PolynomialRing(AbelianRing<E> coefficientRing, String variable) {
        this.coefficientRing = coefficientRing;
        this.variable = variable;
    }

    @Override
    public NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
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

    /**
     * Instantiate a new polynomial in this ring.
     * <p>
     * E.g. to instantiate '5 + 2·x²', call it with {@code ring.newElement(of(5), of(2))}
     * @param coefficients An array of coefficients for the polynomial. The position in the array determines to which power of the variable they apply.
     */
    @SafeVarargs
    public final Polynomial<E> newElement(E... coefficients) {
        E[] trimmed = ArrayUtils.removeTrailingIf(AdditiveMonoidElement::isZero, getCoefficientRing().getElementClass(), coefficients);
        return new Polynomial<>(this, trimmed);
    }


    @Override
    public Polynomial<E> fromString(String input) {
        input = input
            .replaceAll("\\s+", "")
            .replaceAll("-", "+-");
        input = TextUtils.unsuperscript(input);
        if (input.startsWith("+")) {
            input = input.substring(1);
        }

        String[] terms = input.split("\\+");
        Map<Integer, E> coeffs = new HashMap<>();

        Pattern termPattern = Pattern.compile("(.*?)·?(?:" + variable + "\\^?(\\d*))?");
        for (String term : terms) {
            if (term.isEmpty()) continue;
            Matcher m = termPattern.matcher(term);
            if (m.matches()) {
                String coeff = m.group(1);
                String e = m.group(2);
                final int exp;
                if (e == null) {
                    exp = 0;
                } else {
                    if (e.isEmpty()) {
                        exp = 1;
                    } else {
                        exp = Integer.parseInt(e);
                    }
                }
                if (coeff.isEmpty()) {
                    coeffs.put(exp, coefficientRing.one());
                } else if (coeff.equals("-")) {
                    coeffs.put(exp, coefficientRing.one().negation());
                } else {
                    coeffs.put(exp, coefficientRing.fromString(coeff));
                }

            } else {
                throw new IllegalArgumentException("Cannot parse term: " + term);
            }
        }
        int maxExp = coeffs.keySet().stream().max(Integer::compareTo).orElse(0);
        E[] arr = coefficientRing.newArray(maxExp + 1);
        for (int i = 0; i <= maxExp; i++) {
            arr[i] = coeffs.getOrDefault(i, coefficientRing.zero());
        }
        return new Polynomial<>(this, arr);
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
