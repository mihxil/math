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

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OddInteger
    extends AbstractIntegerElement<OddInteger, OddInteger, OddIntegers>
    implements
    MultiplicativeMonoidElement<OddInteger>,
    Scalar<OddInteger>,
    Factoriable<IntegerElement> {


    public static final OddInteger ONE = OddInteger.of(1);

    public static OddInteger of(BigInteger value){
        return OddIntegers.INSTANCE.newElement(value);
    }
    public static OddInteger of(long value){
        return of(BigInteger.valueOf(value));
    }


    OddInteger(BigInteger value) {
        super(OddIntegers.INSTANCE, value);
    }

    @Override
    public OddInteger times(OddInteger multiplier) {
        return with(value.multiply(multiplier.value));
    }

    @Override
    public OddInteger pow(@Positive int n) {
        return super.pow(n);
    }

    @Override
    public OddInteger sqr() {
        return with(value.multiply(value));
    }

    /**
     * Negation can be done, but this addition can't be!
     */
    public OddInteger negation() {
        return with(value.negate());
    }

    public OddInteger plus(EvenInteger summand)  {
        return with(value.add(summand.value));
    }

    @Override
    public OddInteger abs() {
        return with(value.abs());
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    @NonAlgebraic
    public IntegerElement factorial() {
        return new IntegerElement(bigIntegerFactorial());
    }


}
