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

import lombok.Getter;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class NDivisibleIntegers extends
    AbstractIntegers<NDivisibleInteger, NDivisibleInteger, NDivisibleIntegers>
    implements Rng<NDivisibleInteger> {

    private static final Map<Integer, NDivisibleIntegers> INSTANCES = new ConcurrentHashMap<>();


    public static NDivisibleIntegers of(int divisor) {
        return INSTANCES.computeIfAbsent(divisor, NDivisibleIntegers::new);
    }

    @Example(Rng.class)
    public static final NDivisibleIntegers _3Z = NDivisibleIntegers.of(3);

    static NavigableSet<GenericFunction> FUNCTIONS = CollectionUtils.navigableSet(Rng.FUNCTIONS, BasicFunction.ABS);


    @Getter
    final int divisor;

    private final BigInteger bigDivisor;


    private NDivisibleIntegers(int divisor) {
        super();
        this.divisor = divisor;
        this.bigDivisor = BigInteger.valueOf(divisor);
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public NDivisibleInteger zero() {
        return of(BigInteger.ZERO);
    }

    @Override
    public Stream<NDivisibleInteger> stream() {
        return Stream.iterate(zero(),
            i -> i.signum() > 0 ?
                i.negation() :
                i.negation().plus(
                    of(bigDivisor))
        );
    }


    @Override
    public NDivisibleInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random) * divisor));
    }

    @Override
    NDivisibleInteger of(BigInteger value) {
        return new NDivisibleInteger(this, value);
    }

    @Override
    public NDivisibleInteger newElement(BigInteger value) throws InvalidElementCreationException {
        if (! value.remainder(bigDivisor).equals(BigInteger.ZERO)) {
            throw new InvalidElementCreationException("The argument must be dividable by " + divisor + " (" + value + " isn't)");
        }
        return of(value);
    }


    @Override
    public String toString() {
        return divisor + "ℤ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NDivisibleIntegers that = (NDivisibleIntegers) o;

        return divisor == that.divisor;
    }

    @Override
    public int hashCode() {
        return divisor;
    }
}
