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

import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.Scalar;

import static org.meeuw.math.abstractalgebra.integers.PositiveIntegers.INSTANCE;

/**
 * Elements of {@link PositiveIntegers ℕ+}
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class PositiveInteger
    extends
    AbstractIntegerElement<PositiveInteger, PositiveInteger, PositiveIntegers>
    implements
    MultiplicativeMonoidElement<PositiveInteger>,
    AdditiveSemiGroupElement<PositiveInteger>,
    Scalar<PositiveInteger>,
    Ordered<PositiveInteger>,
    Factoriable<PositiveInteger>
{
    public static final PositiveInteger ONE = of(1);


    public static PositiveInteger of(@Positive long value) {
        return PositiveIntegers.INSTANCE.newElement(BigInteger.valueOf(value));
    }

    protected PositiveInteger(@Positive BigInteger value) {
        super(INSTANCE, value);
    }

    @Override
    public PositiveInteger plus(PositiveInteger summand) {
        return with(value.add(summand.value));
    }

    @Override
    public PositiveInteger times(PositiveInteger summand) {
        return with(value.multiply(summand.value));
    }

    @Override
    public PositiveInteger operate(PositiveInteger operand) {
        return times(operand);
    }

    @Override
    public PositiveInteger abs() {
        return this;
    }

    @Override
    public PositiveInteger factorial() {
        return new PositiveInteger(bigIntegerFactorial());
    }

    @Override
    public PositiveInteger subfactorial() {
        return new PositiveInteger(bigIntegerSubfactorial());
    }

    @Override
    public PositiveInteger doubleFactorial() {
        return new PositiveInteger(bigIntegerDoubleFactorial());
    }

}
