package org.meeuw.math.numbers;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;

import java.math.*;
import java.util.stream.Stream;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static org.meeuw.math.Utils.uncertaintyForBigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp class BigDecimalOperations implements UncertaintyNumberOperations<BigDecimal> {

    public static final BigDecimalOperations INSTANCE = new BigDecimalOperations(MathContext.DECIMAL128);

    @Getter
    private final MathContext mathContext;

    @Getter
    private final MathContext uncertaintyMathContext = new MathContext(2, RoundingMode.UP);

    public BigDecimalOperations(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getFractionalUncertainty(BigDecimal value, BigDecimal uncertainty) {
        if (uncertainty.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return uncertainty.divide(value.abs().add(uncertainty), uncertaintyMathContext).stripTrailingZeros();
    }

    @Override
    public BigDecimal sqr(BigDecimal v) {
        return v.multiply(v);
    }

    @Override
    public UncertainNumber<BigDecimal> sqrt(BigDecimal radicand) {
        //return uncertain(radicand.sqrt(mathContext)); // java 9
        return uncertain(BigDecimalMath.sqrt(radicand, mathContext));
    }

    @Override
    public BigDecimal abs(BigDecimal v) {
        return v.abs();
    }

    @Override
    public UncertainNumber<BigDecimal> reciprocal(BigDecimal v) {
        return uncertain(BigDecimal.ONE.divide(v, mathContext));
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
    public BigDecimal multiply(BigDecimal... ns) {
        return Stream.of(ns).reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    @Override
    public UncertainNumber<BigDecimal> ln(BigDecimal bigDecimal) {
        return uncertain(BigDecimalMath.log(bigDecimal, mathContext));
    }

    @Override
    public UncertainNumber<BigDecimal> divide(BigDecimal n1, BigDecimal n2) {
        try {
            return uncertain(n1.divide(n2, mathContext));
        } catch (ArithmeticException ae) {
            throw new DivisionByZeroException(n1, n2, ae);
        }
    }

    protected UncertainNumber<BigDecimal> uncertain(BigDecimal newValue) {
        return ImmutableUncertainNumber.of(newValue, () -> uncertaintyForBigDecimal(newValue, mathContext));
    }

    @Override
    public BigDecimal add(BigDecimal n1, BigDecimal n2) {
        return n1.add(n2);
    }

    @Override
    public BigDecimal add(BigDecimal... n) {
        return Stream.of(n).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal pow(BigDecimal n1, int exponent) {
        return n1.pow(exponent);
    }

    @Override
    public UncertainNumber<BigDecimal> pow(BigDecimal n1, BigDecimal exponent) {
        try {
            return uncertain(BigDecimalMath.pow(n1, exponent, mathContext));
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
    public UncertainNumber<BigDecimal> sin(BigDecimal bigDecimal) {
        return new ImmutableUncertainNumber<>(BigDecimalMath.sin(bigDecimal, mathContext), BigDecimal.ZERO);
    }

    @Override
    public UncertainNumber<BigDecimal> cos(BigDecimal bigDecimal) {
        return uncertain(BigDecimalMath.cos(bigDecimal, mathContext));
    }

    @Override
    public boolean isZero(BigDecimal bigDecimal) {
        return bigDecimal.equals(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal roundingUncertainty(BigDecimal bigDecimal) {
        return Utils.uncertaintyForBigDecimal(bigDecimal, mathContext);
    }
}
