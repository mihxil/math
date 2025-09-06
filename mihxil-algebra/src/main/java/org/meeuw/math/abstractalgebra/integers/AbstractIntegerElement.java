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
package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.IntegerUtils;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.*;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;

/**
 * The abstract element belonging to {@link AbstractIntegers}
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <E> self reference
 * @param <SIZE> the type of the absolute value
 * @param <S> the type of the structure
 *
 */
public abstract class AbstractIntegerElement<
    E extends AbstractIntegerElement<E, SIZE, S>,
    SIZE extends Scalar<SIZE>,
    S extends AbstractIntegers<E, SIZE, S>
    >
    extends AbstractAlgebraicElement<E, S> implements
    SignedNumber<E>,
    AlgebraicElement<E>,
    SizeableScalar<E, SIZE>,
    Ordered<E> {



    @Getter
    protected final BigInteger value;

    /**
     * The constructor, which initializes the backing {@link BigInteger}.
     * This performs no checking on this input, and should therefore remain protected.
     * It is only called when it is sure beforehand that this will result a new value belonging to the given structure.
     * <p>
     * Otherwise calls {@link AbstractIntegers#newElement(BigInteger)}
     */
    protected AbstractIntegerElement(S structure, BigInteger value) {
        super(structure);
        this.value = value;
    }

    /**
     * This is a protected shorthand for creating new elements.
     */
    protected final E with(BigInteger value) {
        return structure.of(value);
    }


    public E pow(@Positive int exponent){
        try {
            return structure.newElement(value.pow(exponent));
        } catch (ArithmeticException ae) {
            throw new IllegalPowerException(ae, BasicAlgebraicIntOperator.POWER.stringify(value.toString(), Integer.toString(exponent)));
        }
    }

    /**
     * Euclidean division of integers.
     * @param divisor integer divisor
     * @return this / divisor
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public E dividedByEuclidean(E divisor) throws DivisionByZeroException {
        if (divisor.value.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException(this, divisor);
        }
        return with(value.divide(divisor.value));
    }

    /**
     * The remainder of Euclidean division of integers.
     * @param divisor integer divisor
     * @return this % divisor
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public E mod(E divisor) throws DivisionByZeroException {
        if (divisor.value.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException(this, divisor);
        }
        return with(value.remainder(divisor.value));
    }

    public RationalNumber dividedBy(E divisor) {
        if (divisor.value.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException(this, divisor);
        }
        return RationalNumber.of(value, divisor.value);
    }

    public E pow(E exponent) throws OverflowException {
        return structure.newElement(IntegerUtils.pow(value, exponent.value));
    }

    public E tetration(int height) {
        return structure.newElement(_tetration(value, height));
    }

    static protected BigInteger _tetration(BigInteger v, int height) {
        if (height < 0) {
            throw new IllegalPowerException("Cannot tetrate with negative", BasicAlgebraicIntOperator.TETRATION.stringify(v.toString(), Integer.toString(height)));
        }
        if (height == 0) {
            return BigInteger.ONE;
        }
        return IntegerUtils.pow(v,_tetration(v, height -1 ));
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value.toString());
    }

    @Override
    public BigInteger bigIntegerValue() {
        return value;
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public int compareTo(@NonNull E f) {
        return value.compareTo(f.value);
    }

    @Override
    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public RationalNumber toRational() {
        return RationalNumber.of(value);
    }

    protected BigInteger bigIntegerFactorial()  {
        Long maxArgument = ConfigurationService.getConfigurationAspect(Factoriable.Configuration.class).getMaxArgument();
        if (maxArgument != null && value.intValue() > maxArgument) {
            throw new InvalidFactorial("Factorial too big (" + value.intValue() + ">" + maxArgument + ")", value.toString());
        }
        return IntegerUtils.bigIntegerFactorial(value);
    }

    protected BigInteger bigIntegerSubfactorial()  {
        Long maxArgument = ConfigurationService.getConfigurationAspect(Factoriable.Configuration.class).getMaxSubArgument();
        if (maxArgument != null && value.intValue() > maxArgument) {
            throw new InvalidFactorial("Factorial too big (" + value.intValue() + ">" + maxArgument + ")", value.toString());
        }
        return IntegerUtils.bigIntegerSubfactorial(value);
    }

     protected BigInteger bigIntegerDoubleFactorial()  {
        Long maxArgument = ConfigurationService.getConfigurationAspect(Factoriable.Configuration.class).getMaxDoubleArgument();
        if (maxArgument != null && value.intValue() > maxArgument) {
            throw new InvalidFactorial("Factorial too big (" + value.intValue() + ">" + maxArgument + ")", value.toString());
        }
        return IntegerUtils.bigIntegerDoubleFactorial(value);
    }



    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        E that = (E) o;

        return value.equals(that.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends AlgebraicElement<F>> Optional<F> castDirectly(Class<F> clazz) {
        if (clazz.isAssignableFrom(IntegerElement.class)) {
            return Optional.of((F) new IntegerElement(getValue()));
        }
        if (clazz.isAssignableFrom(RationalNumber.class)) {
            return Optional.of((F) RationalNumber.of(getValue(), BigInteger.ONE));
        }
        return Optional.empty();
    }

}
