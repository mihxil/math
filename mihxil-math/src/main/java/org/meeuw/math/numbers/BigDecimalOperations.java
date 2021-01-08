package org.meeuw.math.numbers;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp class BigDecimalOperations implements UncertaintyNumberOperations<BigDecimal> {

    public static final BigDecimalOperations INSTANCE = new BigDecimalOperations(MathContext.DECIMAL128);

    private final MathContext mathContext;

    public BigDecimalOperations(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getFractionalUncertainty(BigDecimal value, BigDecimal uncertainty) {
        if (uncertainty.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return uncertainty.divide(value.abs().add(uncertainty), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal sqr(BigDecimal v) {
        return v.multiply(v);
    }

    @Override
    public BigDecimal sqrt(BigDecimal v) {
        //return v.sqrt(mathContext);
        return BigDecimal.valueOf(Math.sqrt(v.doubleValue())).round(mathContext);
    }

    @Override
    public BigDecimal abs(BigDecimal v) {
        return v.abs();
    }

    @Override
    public BigDecimal reciprocal(BigDecimal v) {
        return BigDecimal.ONE.divide(v, mathContext);
    }

    @Override
    public BigDecimal negate(BigDecimal v) {
        return v.negate();
    }

    @Override
    public BigDecimal multiply(BigDecimal n1, BigDecimal n2) {
        return n1.multiply(n2);
    }

    @Override
    public BigDecimal divide(BigDecimal n1, BigDecimal n2) {
        return n1.divide(n2, mathContext);
    }

    @Override
    public BigDecimal add(BigDecimal n1, BigDecimal n2) {
        return n1.add(n2);
    }

    @Override
    public BigDecimal pow(BigDecimal n1, int exponent) {
        return n1.pow(exponent);
    }

    @Override
    public BigDecimal pow(BigDecimal n1, BigDecimal exponent) {
        return BigDecimalMath.pow(n1, exponent, mathContext);
    }

    @Override
    public boolean lt(BigDecimal n1, BigDecimal n2) {
        return n1.compareTo(n2) < 0;
    }

    @Override
    public boolean lte(BigDecimal n1, BigDecimal n2) {
        return n1.compareTo(n2) <= 0;
    }

    @Override
    public boolean isFinite(BigDecimal n1) {
        return true;
    }

    @Override
    public boolean isNaN(BigDecimal n1) {
        return false;
    }

    @Override
    public int signum(BigDecimal bigDecimal) {
        return bigDecimal.signum();
    }

    @Override
    public BigDecimal bigDecimalValue(BigDecimal bigDecimal) {
        return bigDecimal;
    }

    @Override
    public BigDecimal sin(BigDecimal bigDecimal) {
        return BigDecimalMath.sin(bigDecimal, mathContext);
    }

    @Override
    public BigDecimal cos(BigDecimal bigDecimal) {
        return BigDecimalMath.cos(bigDecimal, mathContext);
    }
}
