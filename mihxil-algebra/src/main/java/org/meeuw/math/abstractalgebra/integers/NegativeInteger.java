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

import jakarta.validation.constraints.Max;

import java.math.BigInteger;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.AdditiveSemiGroupElement;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.numbers.SizeableScalar;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class NegativeInteger
    extends  AbstractIntegerElement<NegativeInteger, PositiveInteger, NegativeIntegers>
    implements
    AdditiveSemiGroupElement<NegativeInteger>,
    SizeableScalar<NegativeInteger, PositiveInteger>,
    Ordered<NegativeInteger>
{
    public static final NegativeInteger MINUS_ONE = of(-1);

    public static NegativeInteger of(@Max(-1) long value) {
        return NegativeIntegers.INSTANCE.newElement(BigInteger.valueOf(value));
    }

    protected NegativeInteger(@Max(-1) BigInteger value) {
        super(NegativeIntegers.INSTANCE, value);
    }

    @Override
    public NegativeInteger plus(NegativeInteger summand) {
        return with(value.add(summand.value));
    }

    @Override
    @NonAlgebraic
    public PositiveInteger abs() {
        return new PositiveInteger(value.abs());
    }

    @Override
    public int signum() {
        return -1;
    }

    @Override
    public boolean isZero() {
        return false;
    }

}
