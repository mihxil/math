package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalOperations extends NumberOperations<BigDecimal> {

    public static final BigDecimalOperations INSTANCE = new BigDecimalOperations();


    private MathContext mathContext = MathContext.DECIMAL128;
    @Override
    public BigDecimal sqr(BigDecimal v) {
        return v.multiply(v);
    }

    @Override
    public BigDecimal sqrt(BigDecimal v) {
        //return v.sqrt(mathContext);
        return BigDecimal.valueOf(Math.sqrt(v.doubleValue()));
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
    public boolean lt(BigDecimal n1, BigDecimal n2) {
        return n1.compareTo(n2) < 0;
    }

    @Override
    public boolean lte(BigDecimal n1, BigDecimal n2) {
        return n1.compareTo(n2) <= 0;
    }

    @Override
    public boolean isFinite(BigDecimal n1) {
        return false;
    }

    @Override
    public boolean isNaN(BigDecimal n1) {
        return false;
    }

    @Override
    public BigDecimal bigDecimalValue(BigDecimal bigDecimal) {
        return bigDecimal;
    }
}
