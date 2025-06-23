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
import org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup;
import org.meeuw.math.abstractalgebra.RandomConfiguration;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.BasicFunction;
import org.meeuw.math.operators.GenericFunction;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;

/**
 * The <em>quare</em> integers are an example of a {@link MultiplicativeAbelianSemiGroup}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeAbelianSemiGroup.class)
@Singleton
public class Squares extends AbstractIntegers<Square, Square, Squares>
    implements MultiplicativeAbelianSemiGroup<Square> {

    /**
     * This is the singleton instance of this class. Even integers are a singleton, as they are not parametrized in any way.
     */
    public static final Squares INSTANCE = new Squares();

    static NavigableSet<GenericFunction> FUNCTIONS = CollectionUtils.navigableSet(MultiplicativeAbelianSemiGroup.FUNCTIONS, BasicFunction.ABS);

    private Squares() {
        super(Square.class);
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public Square of(BigInteger value) {
        return new Square(value);
    }

    @Override
    public Square newElement(BigInteger value) throws InvalidElementCreationException {
        return new Square(value);
    }

    public Square zero() {
        return Square.ZERO;
    }

    public Square one() {
        return Square.ONE;
    }


    @Override
    public Stream<Square> stream() {

        return  Stream.iterate(BigInteger.ZERO, (i) -> i.add(ONE))
            .map(i -> new Square(i.multiply(i)));

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
