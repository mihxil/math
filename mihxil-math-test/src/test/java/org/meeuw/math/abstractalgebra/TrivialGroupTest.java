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
package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.GroupTheory;
import org.meeuw.math.abstractalgebra.trivial.TrivialGroupElement;

public class TrivialGroupTest implements GroupTheory<TrivialGroupElement> {
    @Override
    public Arbitrary<? extends TrivialGroupElement> elements() {
        return Arbitraries.of(TrivialGroupElement.e);
    }
}
