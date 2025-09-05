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

import jakarta.validation.constraints.Min;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * The 'Monoid' (multiplicative and additive) of positive integers
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Singleton
public class PositiveIntegers extends AbstractIntegers<PositiveInteger, PositiveInteger, PositiveIntegers>
    implements
    MultiplicativeMonoid<PositiveInteger>,
    MultiplicativeAbelianSemiGroup<PositiveInteger>,
    AdditiveAbelianSemiGroup<PositiveInteger> {


    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(
        MultiplicativeMonoid.OPERATORS,
        AdditiveAbelianSemiGroup.OPERATORS,
        navigableSet(INTEGER_POWER)
    );

    private static final NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(MultiplicativeMonoid.UNARY_OPERATORS, AdditiveAbelianSemiGroup.UNARY_OPERATORS, Factoriable.UNARY_OPERATORS);

    private static final NavigableSet<GenericFunction> FUNCTIONS = navigableSet(MultiplicativeMonoid.FUNCTIONS, AdditiveAbelianSemiGroup.FUNCTIONS, navigableSet(BasicFunction.ABS));


    public static final PositiveIntegers INSTANCE = new PositiveIntegers();

    private PositiveIntegers() {
        super(PositiveInteger.class);
    }



    @Override
    PositiveInteger of(BigInteger value) {
        return new PositiveInteger(value);
    }

    @Override
    public PositiveInteger newElement(@Min(0) BigInteger value) throws InvalidElementCreationException {
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            throw new InvalidElementCreationException("Positive numbers cannot be 0 or negative");
        }
        return of(value);
    }

    @Override
    public PositiveInteger one() {
        return PositiveInteger.ONE;
    }

    @Override
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public boolean operationIsCommutative() {
        return multiplicationIsCommutative();
    }
    @Override
    public boolean isCommutative(AlgebraicBinaryOperator operator) {
        if (operator == BasicAlgebraicBinaryOperator.MULTIPLICATION) {
            return multiplicationIsCommutative();
        }

        if (operator == BasicAlgebraicBinaryOperator.ADDITION) {
            return additionIsCommutative();
        }
        return AlgebraicStructure.defaultIsCommutative(operator, this);

    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }


    @Override
    public Stream<PositiveInteger> stream() {
        return  Stream.iterate(one(), i -> i.plus(one()));
    }

    @Override
    public PositiveInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextNonNegativeLong(random) + 1));
    }


    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("+");
    }
}
