/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup;
import org.meeuw.math.abstractalgebra.RandomConfiguration;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.abstractalgebra.integers.NegativeInteger.MINUS_ONE;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.ADDITION;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.OPERATION;

/**
 * The 'Semigroup'  of  negative numbers
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(AdditiveAbelianSemiGroup.class)
public class NegativeIntegers
    extends AbstractIntegers<NegativeInteger, PositiveInteger, NegativeIntegers>
    implements
    AdditiveAbelianSemiGroup<NegativeInteger> {

    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(OPERATION, ADDITION);

    public static final NegativeIntegers INSTANCE = new NegativeIntegers();

    protected NegativeIntegers() {
        super(NegativeInteger.class);
    }

    @Override
    NegativeInteger of(BigInteger value) {
        return new NegativeInteger(value);
    }

    @Override
    public NegativeInteger newElement(@Max(-1) BigInteger value) throws InvalidElementCreationException {
        if (value.compareTo(BigInteger.ZERO) >= 0) {
            throw new InvalidElementCreationException("Negative numbers cannot be 0 or positive");
        }
        return of(value);
    }

    @Override
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public Stream<NegativeInteger> stream() {
        return  Stream.iterate(MINUS_ONE, i -> i.plus(MINUS_ONE));
    }

    @Override
    public NegativeInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextNonNegativeLong(random) * -1 - 1));
    }


    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("-");
    }
}
