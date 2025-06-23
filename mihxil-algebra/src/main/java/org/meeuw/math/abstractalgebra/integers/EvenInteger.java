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

import java.math.BigInteger;

import org.checkerframework.checker.index.qual.Positive;
import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.RngElement;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.Scalar;

/**
 * Elements of {@link EvenIntegers 2ℤ}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenInteger
    extends AbstractIntegerElement<EvenInteger, EvenInteger, EvenIntegers>
    implements
    RngElement<EvenInteger>,
    Scalar<EvenInteger> {

    public static final EvenInteger ZERO = new EvenInteger(BigInteger.ZERO);
    public static final EvenInteger TWO = new EvenInteger(IntegerUtils.TWO);


    public static EvenInteger of(BigInteger value) throws InvalidElementCreationException {
        return EvenIntegers.INSTANCE.newElement(value);
    }
    public static EvenInteger of(long value) throws InvalidElementCreationException {
        return of(BigInteger.valueOf(value));
    }

    EvenInteger(BigInteger value) {
        super(EvenIntegers.INSTANCE, value);
    }


    public OddInteger plus(OddInteger summand) {
        return new OddInteger(value.add(summand.getValue()));
    }

    @Override
    public EvenInteger negation() {
        return new EvenInteger(value.negate());
    }

    @Override
    public EvenInteger minus(EvenInteger subtrahend) {
        return of(value.subtract(subtrahend.value));
    }


    @Override
    public EvenInteger times(EvenInteger multiplier) {
        return of(value.multiply(multiplier.value));
    }

    @Override
    public EvenInteger pow(@Positive int exponent) {
        if (exponent == 0) {
            throw new IllegalPowerException("Cannot raise to 0",  this + "^0");
        }
        return super.pow(exponent);
    }

    @Override
    public EvenInteger sqr() {
        return of(value.multiply(value));
    }

    @Override
    public EvenInteger abs() {
        return of(value.abs());
    }

    @Override
    public EvenInteger plus(EvenInteger summand) {
        return of(value.add(summand.value));
    }
}
