package org.meeuw.math.numbers;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.*;

import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

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
        if (uncertainty.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return uncertainty.divide(value.abs().add(uncertainty), mathContext);
    }

    @Override
    public BigDecimal sqr(BigDecimal v) {
        return v.multiply(v);
    }

    @Override
    public BigDecimal sqrt(BigDecimal radicand) {
        //return v.sqrt(mathContext);
        return BigDecimal.valueOf(Math.sqrt(radicand.doubleValue())).round(mathContext);
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
        try {
            return n1.divide(n2, mathContext);
        } catch (ArithmeticException ae) {
            throw new DivisionByZeroException(n1, n2, ae);
        }
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
        try {
            return BigDecimalMath.pow(n1, exponent, mathContext);
        } catch (ArithmeticException ae) {
            throw new ReciprocalException(ae);
        }
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
