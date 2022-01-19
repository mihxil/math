package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.util.Arrays;

import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * This interface generalises numeric operations for  {@link BigDecimal} and {@link Double}.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberOperations<N extends Number> {

    @SuppressWarnings("unchecked")
    static <N extends Number> NumberOperations<N> of(N n) {
        if (n instanceof BigDecimal) {
            return (NumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (NumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }

    N  getFractionalUncertainty(N value, N unc);

    N sqr(N v);

    UncertainNumber<N> sqrt(N radicand);

    N abs(N v);

    N reciprocal(N v);

    N negate(N v);

    N multiply(N n1, N n2);

    @SuppressWarnings("unchecked")
    N multiply(N... n1);

    N ln(N n);

    default N multiply(int n1, N n2) {
        N result = n2;
        for (int i = n1; i > 1; i--) {
            result = add(result, n2);
        }
        return result;
    }

    UncertainNumber<N> divide(N n1, N n2);

    N add(N n1, N n2);

    @SuppressWarnings("unchecked")
    N add(N... n);

    default N minus(N n1, N n2) {
        return add(n1, negate(n2));
    }

    N pow(N n1, int exponent);

    UncertainNumber<N> pow(N n1, N exponent);

    boolean lt(N n1, N n2);

    boolean lte(N n1, N n2);

    default boolean gt(N n1, N n2) {
        return ! lte(n1, n2);
    }

    default boolean gte(N n1, N n2) {
        return ! lt(n1, n2);
    }

    default int compare(N n1, N n2) {
        if (lt(n1, n2)) {
            return -1;
        } else if (gt(n1, n2)) {
            return 1;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    default N max(N... n) {
        return Arrays.stream(n).max(this::compare).orElse(null);
    }

    @SuppressWarnings("unchecked")
    default N min(N... n) {
        return Arrays.stream(n).min(this::compare).orElse(null);
    }

    boolean isFinite(N n1);

    boolean isNaN(N n1);

    int signum(N n);

    BigDecimal bigDecimalValue(N n);

    UncertainNumber<N> sin(N n);

    UncertainNumber<N> cos(N n);

    boolean isZero(N n);

}
