/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.numbers;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static org.meeuw.configuration.ConfigurationService.withAspect;
import static org.meeuw.math.Utils.uncertaintyForBigDecimal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp class BigDecimalOperations implements UncertaintyNumberOperations<BigDecimal> {

    public static final BigDecimalOperations INSTANCE = new BigDecimalOperations();

    private BigDecimalOperations() {
    }


    @Override
    public BigDecimal getFractionalUncertainty(BigDecimal value, BigDecimal uncertainty) {
        if (uncertainty.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return uncertainty.divide(value.abs().add(uncertainty), uncertaintyContext()).stripTrailingZeros();

    }

    @Override
    public BigDecimal sqr(BigDecimal v) {
        return v.multiply(v);
    }

    @Override
    public UncertainNumber<BigDecimal> sqrt(BigDecimal radicand) {
        //return uncertain(radicand.sqrt(mathContext)); // java 9
        try {
            return uncertain(BigDecimalMath.sqrt(radicand, context()));
        } catch (ArithmeticException arithmeticException) {
            throw new IllegalSqrtException(arithmeticException);
        }
    }

    @Override
    public BigDecimal abs(BigDecimal v) {
        return v.abs();
    }

    @Override
    public UncertainNumber<BigDecimal> reciprocal(BigDecimal v) {
        return uncertain(BigDecimal.ONE.divide(v, context()));
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
    public UncertainNumber<BigDecimal> ln(BigDecimal bigDecimal) throws IllegalLogarithmException {
        try {
            return uncertain(BigDecimalMath.log(bigDecimal, context()));
        } catch(ArithmeticException a) {
            throw new IllegalLogarithmException(a);
        }
    }

    @Override
    public UncertainNumber<BigDecimal> divide(BigDecimal n1, BigDecimal n2) {
        try {
            return uncertain(n1.divide(n2, context()));
        } catch (ArithmeticException ae) {
            throw new DivisionByZeroException(n1, n2, ae);
        }
    }


    protected UncertainNumber<BigDecimal> uncertain(BigDecimal newValue) {
        return ImmutableUncertainNumber.of(
            newValue,
            () -> uncertaintyForBigDecimal(newValue, context())
        );
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
    public UncertainNumber<BigDecimal> exp(BigDecimal e) {
        return uncertain(BigDecimalMath.exp(e, context()));
    }

    @Override
    public UncertainNumber<BigDecimal> pow(BigDecimal n1, BigDecimal exponent) throws ReciprocalException {
        try {
            return uncertain(BigDecimalMath.pow(n1, exponent, context()));
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
        return uncertain(BigDecimalMath.sin(bigDecimal, context()));
    }

    @Override
    public UncertainNumber<BigDecimal> cos(BigDecimal bigDecimal) {
        return uncertain(BigDecimalMath.cos(bigDecimal, context()));
    }

    @Override
    public UncertainNumber<BigDecimal> atan2(BigDecimal y, BigDecimal x) {
        return uncertain(BigDecimalMath.atan2(y, x, context()));
    }

    @Override
    public boolean isZero(BigDecimal bigDecimal) {
        return bigDecimal.equals(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal roundingUncertainty(BigDecimal bigDecimal) {
        return Utils.uncertaintyForBigDecimal(bigDecimal, context());
    }

    @Override
    public  <X> X withUncertaintyContext(Supplier<X> supplier) {
        return withAspect(MathContextConfiguration.class,
            (mc) -> mc.withContext(uncertaintyContext()), supplier);
    }

    private MathContext context() {
        return MathContextConfiguration.get().getContext();
    }

    private MathContext uncertaintyContext() {
        return MathContextConfiguration.get().getUncertaintyContext();
    }
}
