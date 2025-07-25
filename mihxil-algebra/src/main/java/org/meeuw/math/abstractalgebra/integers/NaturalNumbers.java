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

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.IDENTIFY;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;
import static org.meeuw.math.operators.BasicFunction.ABS;

/**
 * The 'Monoid' (multiplicative and additive) of natural numbers.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(AdditiveMonoid.class)
@Singleton
public class NaturalNumbers extends AbstractIntegers<NaturalNumber, NaturalNumber, NaturalNumbers>
    implements
    MultiplicativeMonoid<NaturalNumber>,
    AdditiveMonoid<NaturalNumber>,
    AdditiveAbelianSemiGroup<NaturalNumber>,
    MultiplicativeAbelianSemiGroup<NaturalNumber> {



    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(OPERATION, MULTIPLICATION, ADDITION);

    private static final NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(navigableSet(IDENTIFY, SQR), Factoriable.UNARY_OPERATORS);


    private static final NavigableSet<GenericFunction> FUNCTIONS = navigableSet(ABS);

    public static final NaturalNumbers INSTANCE = new NaturalNumbers();

    private NaturalNumbers() {
        super(NaturalNumber.class);
    }


    @Override
    NaturalNumber of(BigInteger value) {
        return new NaturalNumber(value);
    }

    @Override
    public NaturalNumber newElement(@Min(0) BigInteger value) throws InvalidElementCreationException {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new InvalidElementCreationException("Natural numbers must be non-negative");
        }
        return of(value);
    }

    @Override
    public NaturalNumber zero() {
        return NaturalNumber.ZERO;
    }

    @Override
    public NaturalNumber one() {
        return NaturalNumber.ONE;
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
    public boolean operationIsCommutative() {
        return MultiplicativeMonoid.super.operationIsCommutative();
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public Stream<NaturalNumber> stream() {
        return Stream.iterate(zero(), i -> i.plus(one()));
    }

    @Override
    public NaturalNumber nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random)));
    }

    @Override
    public String toString() {
        return "ℕ";
    }
}
