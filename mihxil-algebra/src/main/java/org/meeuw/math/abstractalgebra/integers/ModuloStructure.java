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

import lombok.Getter;

import java.util.Random;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.meeuw.math.Randomizable;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidStructureCreationException;

/**
 * Implementation of {@code ℤ/nℤ}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class ModuloStructure<E extends ModuloElement<E, S>, S extends ModuloStructure<E, S>>
    extends AbstractAlgebraicStructure<E>
    implements AbelianRing<E>, Streamable<E>, Randomizable<E> {

    @Getter
    final long divisor;
    @Getter
    final Cardinality cardinality;

    final E one;
    final E zero;

    ModuloStructure(Class<E> eClass, long divisor) {
        super(eClass);
        if (divisor <= 0) {
            throw new InvalidStructureCreationException("Divisor of modulo structure must be > 0");
        }
        this.divisor = divisor;
        this.cardinality = Cardinality.of(divisor);
        one = element(1);
        zero = element(0);
    }

    @Override
    public E one() {
        return one;
    }

    @Override
    public E zero() {
        return zero;
    }


    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public Stream<E> stream() {
        return LongStream.range(0, divisor).mapToObj(this::element);
    }

    @Override
    public E nextRandom(Random random) {
        return element(Math.abs(random.nextLong() % divisor));
    }

    public Stream<String> multiplicationTable(String format) {
        return stream().flatMap(e1 ->
            stream().map(e2 -> String.format(format, e1, e2, e1.times(e2)))
        );
    }

    /**
     * Streams the entire multiplication table of the modulo structure
     */
    public Stream<String> multiplicationTable() {
        return multiplicationTable("%s x %s = %s");
    }


    abstract E element(long v);

    @Override
    public String toString() {
        return "ℤ/" + divisor + "ℤ";
    }

}
