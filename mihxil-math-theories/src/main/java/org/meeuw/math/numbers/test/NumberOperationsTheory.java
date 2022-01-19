package org.meeuw.math.numbers.test;

import java.math.BigDecimal;

import net.jqwik.api.*;

import org.meeuw.math.numbers.NumberOperations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
public interface NumberOperationsTheory<N extends Number> {

    String NUMBERS = "numbers";

    @Provide
    Arbitrary<? extends N> numbers();

    NumberOperations<N> operations();

    @Property
    default void  getFractionalUncertainty(N value, N unc) {


    }

    @Property
    default void sqr(@ForAll(NUMBERS) N v) {
        assertThat(operations().sqr(v).doubleValue()).isEqualTo(v.doubleValue() * v.doubleValue());
    }

    @Property
    default void sqrt(N radicand) {
        assertThat(operations().sqrt(radicand).getValue().doubleValue()).isEqualTo(Math.sqrt(radicand.doubleValue()));

    }

    N abs(N v);

    N reciprocal(N v);

    N negate(N v);

    N multiply(N n1, N n2);

    default N multiply(int n1, N n2) {
        N result = n2;
        for (int i = n1; i > 1; i--) {
            result = add(result, n2);
        }
        return result;
    }

    N divide(N n1, N n2);

    N add(N n1, N n2);

    default N minus(N n1, N n2) {
        return add(n1, negate(n2));
    }

    N pow(N n1, int exponent);

    N pow(N n1, N exponent);

    boolean lt(N n1, N n2);

    boolean lte(N n1, N n2);

    default boolean gt(N n1, N n2) {
        return ! lte(n1, n2);
    }

    default boolean gte(N n1, N n2) {
        return ! lt(n1, n2);
    }

    boolean isFinite(N n1);

    boolean isNaN(N n1);

    int signum(N n);

    BigDecimal bigDecimalValue(N n);

    N sin(N n);

    N cos(N n);
}
