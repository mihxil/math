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
import org.meeuw.math.operators.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.abstractalgebra.integers.AbstractIntegerElement.BigTWO;
import static org.meeuw.math.abstractalgebra.integers.OddInteger.ONE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeMonoid.class)
@Example(MultiplicativeAbelianSemiGroup.class)
public class OddIntegers extends AbstractIntegers<OddInteger, OddInteger, OddIntegers>
    implements
    MultiplicativeMonoid<OddInteger>,
    MultiplicativeAbelianSemiGroup<OddInteger>{

    public static final OddIntegers INSTANCE = new OddIntegers();

    static NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = CollectionUtils.navigableSet(MultiplicativeMonoid.UNARY_OPERATORS, BasicAlgebraicUnaryOperator.NEGATION);

    static NavigableSet<GenericFunction> FUNCTIONS = CollectionUtils.navigableSet(MultiplicativeMonoid.FUNCTIONS, BasicFunction.ABS);



    private OddIntegers() {
        super(OddInteger.class);
    }

    @Override
    public NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public OddInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random) * 2 + 1));
    }

    @Override
    OddInteger of(BigInteger value) {
        return new OddInteger(value);
    }

    @Override
    public OddInteger newElement(BigInteger value) throws InvalidElementCreationException {
        if (value.remainder(BigTWO).equals(BigInteger.ZERO)) {
            throw new InvalidElementCreationException("The argument must be odd (" + value + " isn't)");
        }
        return of(value);
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public OddInteger one() {
        return ONE;
    }

    @Override
    public Stream<OddInteger> stream() {
        return Stream.iterate(
            one(),
            i -> i.signum() > 0 ?
                i.negation() :
                i.negation().plus(EvenInteger.TWO)
        );
    }

    @Override
    public String toString() {
        return "ℕ" + TextUtils.subscript("o");
    }

}
