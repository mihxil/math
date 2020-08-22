package org.meeuw.math.numbers;

import java.math.BigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class NumberOperations<N extends Number> {

    public static <N extends Number> NumberOperations<N> of(N n) {
        if (n instanceof BigDecimal) {
            return (NumberOperations<N>) BigDecimalOperations.INSTANCE;
        } else {
            return (NumberOperations<N>) DoubleOperations.INSTANCE;
        }
    }

    public abstract N sqr(N v);

    public abstract N sqrt(N v);

    public abstract N abs(N v);

    public abstract N reciprocal(N v);

    public abstract N negate(N v);

    public abstract N multiply(N n1, N n2);

    public N multiply(int n1, N n2) {
        N result = n2;
        for (int i = n1; i > 1; i--) {
            result = add(result, n2);
        }
        return result;
    }


    public abstract N divide(N n1, N n2);

    public abstract N add(N n1, N n2);

    public  N minus(N n1, N n2) {
        return add(n1, negate(n2));
    }

    public abstract N pow(N n1, int exponent);

    public abstract boolean lt(N n1, N n2);

    public abstract boolean lte(N n1, N n2);

    public  boolean gt(N n1, N n2) {
        return ! lte(n1, n2);
    }

    public boolean gte(N n1, N n2) {
        return ! lt(n1, n2);
    }

    public abstract boolean isFinite(N n1);

    public abstract boolean isNaN(N n1);

    public abstract  BigDecimal bigDecimalValue(N n);


    public N multiplyUncertainty(N multiplier, N uncertainty) {
        return multiply(abs(multiplier),  uncertainty);
    }

}
