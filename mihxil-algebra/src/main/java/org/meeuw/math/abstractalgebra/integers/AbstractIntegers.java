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
import java.util.*;

import org.meeuw.math.Randomizable;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.operators.*;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredBinaryMethod;

/**
 * An abstract super structure for various integer types.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public abstract class AbstractIntegers<
    E extends AbstractIntegerElement<E, SIZE, S>,
    SIZE extends Scalar<SIZE>,
    S extends AbstractIntegers<E, SIZE, S>
    >
    extends AbstractAlgebraicStructure<E>  implements
    Streamable<E> ,
    Randomizable<E> {

    public static AlgebraicBinaryOperator INTEGER_POWER = new SimpleAlgebraicBinaryOperator(
        getDeclaredBinaryMethod(AbstractIntegerElement.class, "pow"),
        BasicAlgebraicBinaryOperator.POWER
    );



    protected AbstractIntegers(Class<E> clazz) {
        super(clazz);
    }
    protected AbstractIntegers() {
        super();
    }

    @Override
    public NavigableSet<AlgebraicComparisonOperator> getSupportedComparisonOperators() {
        return BasicComparisonOperator.ALL_AND_EQUALS;
    }

    @Override
    public Set<AlgebraicStructure<?>> getSuperGroups() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Integers.INSTANCE,
            RationalNumbers.INSTANCE
        )));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    /**
     * The internal way to create a new element
    */
    abstract E of(BigInteger value);

    /*
     * @throws InvalidElementCreationException if the given backing value does not fit in the structure. E.g. the structure is {@link PositiveIntegers}, and the value is negative.
     */
    public abstract E newElement(BigInteger value) throws InvalidElementCreationException;



    @Override
    public E fromString(String string) {
        return of(new BigInteger(string));
    }

}
