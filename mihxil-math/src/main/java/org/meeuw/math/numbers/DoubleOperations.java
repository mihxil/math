package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.meeuw.math.Utils;
import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp class DoubleOperations implements UncertaintyNumberOperations<Double> {

    public static final DoubleOperations INSTANCE = new DoubleOperations();

    @Override
    public Double getFractionalUncertainty(Double value, Double uncertainty) {
        double u = Math.max(uncertainty, Utils.uncertaintyForDouble(value));
        return u  / (abs(value) + u); // add uncertainty to avoid division by zero.
    }

    @Override
    public Double sqr(Double v) {
        return v * v;
    }

    @Override
    public UncertainNumber<Double> sqrt(Double radicand) {
        return uncertain(Math.sqrt(radicand));
    }

    @Override
    public Double abs(Double v) {
        return Math.abs(v);
    }

    @Override
    public UncertainNumber<Double> reciprocal(Double v) {
        return divide(1d, v);
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
    public Double multiply(Double... ns) {
        return Stream.of(ns).reduce(1d, (a, b) -> a * b);
    }

    @Override
    public UncertainNumber<Double> divide(Double n1, Double n2) {
        if (n2 == 0d) {
            throw new ArithmeticException("division by zero");
        }
        return uncertain(n1 / n2);
    }

    @Override
    public Double add(Double n1, Double n2) {
        return n1 + n2;
    }

    @Override
    public Double add(Double... ns) {
        return Stream.of(ns).reduce(0d, Double::sum);
    }

    @Override
    public Double pow(Double n1, int exponent) {
        return Math.pow(n1, exponent);
    }

    @Override
    public UncertainNumber<Double> pow(Double n1, Double exponent) {
        return uncertain(Math.pow(n1, exponent));
    }

    @Override
    public boolean lt(Double n1, Double n2) {
        return n1 < n2;
    }

    @Override
    public boolean lte(Double n1, Double n2) {
        return n1 <= n2;
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

    @Override
    public UncertainNumber<Double> sin(Double aDouble) {
        return uncertain(Math.sin(aDouble));
    }

    @Override
    public UncertainNumber<Double> cos(Double aDouble) {
        return uncertain(Math.cos(aDouble));
    }

    @Override
    public boolean isZero(Double aDouble) {
        return aDouble == 0d;
    }

    @Override
    public UncertainNumber<Double> ln(Double v) {
        return uncertain(Math.log(v));
    }

    protected UncertainNumber<Double> uncertain(Double newValue) {
        return ImmutableUncertainNumber.of(newValue, uncertaintyForDouble(newValue));
    }

    @Override
    public Double roundingUncertainty(Double aDouble) {
        return Utils.uncertaintyForDouble(aDouble);
    }
}
