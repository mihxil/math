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
package org.meeuw.math.abstractalgebra.trivial;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * The trivial ring has one element, which is both zero and one.
 *
 * <pre>
 * e + e = e
 * e · e = e
 * </pre>
 */
public class TrivialRing implements AbelianRing<TrivialRingElement>, Streamable<TrivialRingElement> {

    public  static final TrivialRing INSTANCE = new TrivialRing();

    @Override
    public TrivialRingElement zero() {
        return TrivialRingElement.e;
    }

    @Override
    public TrivialRingElement one() {
        return TrivialRingElement.e;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ONE;
    }

    @Override
    public Class<TrivialRingElement> getElementClass() {
        return TrivialRingElement.class;
    }

    @Override
    public String toString() {
        return "{0}";

    }

    @Override
    public Stream<TrivialRingElement> stream() {
        return Stream.of(TrivialRingElement.e);
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

}
