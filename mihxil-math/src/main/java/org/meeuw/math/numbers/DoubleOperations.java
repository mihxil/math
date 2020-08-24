package org.meeuw.math.numbers;

import java.math.BigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp class DoubleOperations implements UncertaintyNumberOperations<Double> {

    public static final DoubleOperations INSTANCE = new DoubleOperations();

    @Override
    public Double sqr(Double v) {
        return v * v;
    }

    @Override
    public Double sqrt(Double v) {
        return Math.sqrt(v);
    }

    @Override
    public Double abs(Double v) {
        return Math.abs(v);
    }

    @Override
    public Double reciprocal(Double v) {
        return 1 / v;
    }

    @Override
    public Double negate(Double v) {
        return -1 * v;
    }

    @Override
    public Double multiply(Double n1, Double n2) {
        return n1 * n2;
    }

    @Override
    public Double divide(Double n1, Double n2) {
        return n1 / n2;
    }

    @Override
    public Double add(Double n1, Double n2) {
        return n1 + n2;
    }

    @Override
    public Double pow(Double n1, int exponent) {
        return Math.pow(n1, exponent);
    }

    @Override
    public boolean lt(Double n1, Double n2) {
        return n1 < n2;
    }

    @Override
    public boolean lte(Double n1, Double n2) {
        return n2 <= n2;
    }

    @Override
    public boolean isFinite(Double n1) {
        return Double.isFinite(n1);
    }

    @Override
    public boolean isNaN(Double n1) {
        return Double.isNaN(n1);
    }

    @Override
    public int signum(Double aDouble) {
        return (int) Math.signum(aDouble);
    }

    @Override
    public BigDecimal bigDecimalValue(Double d) {
        return BigDecimal.valueOf(d);
    }
}
