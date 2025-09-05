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
import static org.meeuw.math.IntegerUtils.TWO;
import static org.meeuw.math.abstractalgebra.integers.EvenInteger.ZERO;

/**
 * The <em>even</em> integers are an example of a {@link Rng}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(Rng.class)
@Singleton
public class EvenIntegers extends AbstractIntegers<EvenInteger, EvenInteger, EvenIntegers>
    implements Rng<EvenInteger>, MultiplicativeAbelianSemiGroup<EvenInteger> {

    /**
     * This is the singleton instance of this class. Even integers are a singleton, as they are not parametrized in any way.
     */
    public static final EvenIntegers INSTANCE = new EvenIntegers();

    static NavigableSet<GenericFunction> FUNCTIONS = CollectionUtils.navigableSet(Rng.FUNCTIONS, BasicFunction.ABS);

    private EvenIntegers() {
        super(EvenInteger.class);
    }


    public NavigableSet<AlgebraicComparisonOperator> getSupportedOperations() {
        return BasicComparisonOperator.ALL_AND_EQUALS;
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public EvenInteger of(BigInteger value) {
        return new EvenInteger(value);
    }

    @Override
    public EvenInteger newElement(BigInteger value) throws InvalidElementCreationException {
        if (value.remainder(TWO).equals(ONE)) {
            throw new InvalidElementCreationException("The argument must be even (" + value + " isn't)");
        }
        return new EvenInteger(value);
    }

    @Override
    public EvenInteger zero() {
        return ZERO;
    }

    @Override
    public Stream<EvenInteger> stream() {
        return Stream.iterate(
            zero(),
            i -> i.signum() > 0 ?
                i.negation() :
                i.negation().plus(EvenInteger.TWO)
        );
    }

    @Override
    public EvenInteger nextRandom(Random random) {
        return of(valueOf(RandomConfiguration.nextLong(random) * 2));
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public String toString() {
        return "2ℤ";
    }
}
