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
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;

/**
 * The <em>quare</em> integers are an example of a {@link MultiplicativeAbelianSemiGroup}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeMonoid.class)
@Singleton
public class Squares extends AbstractIntegers<Square, Square, Squares>
    implements MultiplicativeMonoid<Square>, MultiplicativeAbelianSemiGroup<Square> {

    /**
     * This is the singleton instance of this class. Even integers are a singleton, as they are not parametrized in any way.
     */
    public static final Squares INSTANCE = new Squares();

    static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(
        MultiplicativeSemiGroup.OPERATORS,
        INTEGER_POWER
    );

    static NavigableSet<GenericFunction> FUNCTIONS = CollectionUtils.navigableSet(
        MultiplicativeMonoid.FUNCTIONS, BasicFunction.ABS);

    private Squares() {
        super(Square.class);
    }

     @Override
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public Square of(@org.meeuw.math.validation.Square BigInteger value) {
        return Square.of(value);
    }

    @Override
    public Square newElement(@org.meeuw.math.validation.Square BigInteger value) throws InvalidElementCreationException {
        return Square.of(value);
    }

    @Synonym("0")
    public Square zero() {
        return Square.ZERO;
    }

    @Override
    @Synonym("1")
    public Square one() {
        return Square.ONE;
    }



    @Override
    public Stream<Square> stream() {

        return  Stream.iterate(BigInteger.ZERO, (i) -> i.add(ONE))
            .map(i -> of(i.multiply(i)));

    }

    @Override
    public Square nextRandom(Random random) {
        return of(valueOf(RandomConfiguration.nextLong(random) * 2));
    }



    @Override
    public String toString() {
        return SQR.stringify("ℤ");
    }
}
