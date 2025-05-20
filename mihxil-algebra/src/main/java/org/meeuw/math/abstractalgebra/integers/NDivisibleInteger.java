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

import org.meeuw.math.abstractalgebra.GroupElement;
import org.meeuw.math.abstractalgebra.RngElement;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.numbers.Scalar;

/**
 * Element of {@link NDivisibleIntegers nℤ}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NDivisibleInteger
    extends AbstractIntegerElement<NDivisibleInteger, NDivisibleInteger, NDivisibleIntegers>
    implements
    RngElement<NDivisibleInteger>,
    Scalar<NDivisibleInteger>,
    GroupElement<NDivisibleInteger> {


    public static NDivisibleInteger of(int divisor, long value){
        return NDivisibleIntegers.of(divisor).newElement(BigInteger.valueOf(value));
    }

    NDivisibleInteger(NDivisibleIntegers structure, long value) {
        this(structure, BigInteger.valueOf(value));
    }

    NDivisibleInteger(NDivisibleIntegers structure, BigInteger value) {
        super(structure, value);
    }

    @Override
    public NDivisibleInteger plus(NDivisibleInteger summand) {
        return with(value.add(summand.value));
    }

    @Override
    public NDivisibleInteger negation() {
        return with(value.negate());
    }

    @Override
    public NDivisibleInteger minus(NDivisibleInteger subtrahend) {
        return with(value.subtract(subtrahend.value));
    }

    @Override
    public NDivisibleInteger times(NDivisibleInteger multiplier) {
        return with(value.multiply(multiplier.value));
    }

    @Override
    public NDivisibleInteger pow(@Positive int exponent) {
        if (exponent == 0 && structure.divisor != 1) {
            throw new IllegalPowerException("Cannot raise to 0", this + "^0");
        }
        return super.pow(exponent);
    }

    @Override
    public NDivisibleInteger sqr() {
        return with(value.multiply(value));
    }

    @Override
    public NDivisibleInteger abs() {
        return with(value.abs());
    }

}
