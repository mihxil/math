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
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import static org.assertj.core.api.Assertions.assertThat;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;
import org.meeuw.util.test.ElementTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface StrictlyOrderedTheory<E extends StrictlyOrdered<E>> extends ElementTheory<E> {

    @Property
    default void orderedReflexive(@ForAll(ELEMENTS) E e) {
        assertThat(e.lte(e)).isTrue();
    }

    @Property
    default void strictOrderedTransitive(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b, @ForAll(ELEMENTS) E c) {
        if (a.lt(b)) {
            // a < b
            if (b.lt(c)) {
                // a < b, b < c ->  a < c
                assertThat(a.lt(c)).withFailMessage("%s < %s and %s = %s -> %s < %s", a, b, b, c, a, c).isTrue();
            } else {
                getLogger().debug("a < b,   b !<  c");
            }
        } else if (b.lt(a)) {
            // b < a
            if (a.lt(c)) {
                // b < a, a < c
                assertThat(b.lt(c)).withFailMessage("%s < %s and %s < %s -> %s < %s", b, a, a, c, b, c).isTrue();
            } else {
                // b < a, a > c
                getLogger().debug("b > a,  a < c");
            }
        }
    }

    @Property
    default void orderedAntisymmetric(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        if (a.lte(b) && b.lte(a)) {
            assertThat(a.equals(b)).isTrue();
        }
    }

    @Property
    default void orderedStronglyConnected(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.lte(b) || b.lte(a)).withFailMessage("%s <= %s = %s %s <= %s = %s, on of these must be true", a, b, a.lte(b), b, a,b.lte(a)).isTrue();
    }
}
