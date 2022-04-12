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
package org.meeuw.test.math.abstractalgebra.integers;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.integers.ModuloRing;
import org.meeuw.math.abstractalgebra.integers.ModuloRingElement;
import org.meeuw.math.abstractalgebra.test.RingTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ModuloRingElementTest implements RingTheory<ModuloRingElement> {

    @Override
    @Provide
    public Arbitrary<ModuloRingElement> elements() {
        ModuloRing structure = ModuloRing.of(10);
        return Arbitraries.integers()
            .between(0, 10).map(i -> new ModuloRingElement(i, structure))
            .injectDuplicates(0.1)
            ;
    }
}
