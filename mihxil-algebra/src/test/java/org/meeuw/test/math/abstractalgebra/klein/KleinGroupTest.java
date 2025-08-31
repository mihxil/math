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
package org.meeuw.test.math.abstractalgebra.klein;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.klein.KleinElement;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.theories.abstractalgebra.GroupTheory;

import static org.assertj.core.api.Assertions.assertThat;

public class KleinGroupTest implements GroupTheory<KleinElement> {
    @Override
    public Arbitrary<KleinElement> elements() {
        return Arbitraries.of(KleinElement.values());
    }

    @Test
    public void operators() {
        assertThat(KleinGroup.INSTANCE.getSupportedOperators().toString()).isEqualTo("[OPERATION]");
    }


}
