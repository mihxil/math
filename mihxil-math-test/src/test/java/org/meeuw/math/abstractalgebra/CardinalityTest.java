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
package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.MultiplicativeSemiGroupTheory;

public class CardinalityTest implements MultiplicativeSemiGroupTheory<Cardinality> {
    @Override
    public Arbitrary<? extends Cardinality> elements() {
        return Arbitraries.of(
            Cardinality.ALEPH_0,
            Cardinality.ALEPH_1,
            Cardinality.C,
            Cardinality.ONE,
            Cardinality.ALEPH_0.times(Cardinality.of(4)),
            Cardinality.of(2),
            Cardinality.of(0),
            Cardinality.of(3),
            Cardinality.of(100)
        );
    }
}
