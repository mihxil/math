package org.meeuw.math.numbers;

import java.math.BigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberOperations<N extends Number> {

    static <N extends Number> NumberOperations<N> of(N n) {
        if (n instanceof BigDecimal) {
            return (NumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (NumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }

    N sqr(N v);

    N sqrt(N v);

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



}
