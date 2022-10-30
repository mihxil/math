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
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;
import org.meeuw.math.exceptions.NotComparableException;
import org.meeuw.math.uncertainnumbers.Uncertain;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface OrderedTheory<E extends StrictlyOrdered<E>> extends StrictlyOrderedTheory<E> {


    @Property
    default void orderedTransitive(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b, @ForAll(ELEMENTS) E c) {
        if (a instanceof Ordered) {
            assertThat(a).isNotInstanceOf(Uncertain.class);
            orderedTransitiveImpl(a, b, c);
        } else {
            assertThat(a).isInstanceOf(Uncertain.class);
            try {
                orderedTransitiveImpl(a, b, c);
            } catch (AssertionFailedError afe) {
                getLogger().info(afe.getMessage());
            }
        }
    }

    default void orderedTransitiveImpl(E a, E b, E c) {
        try {
            if (a.lte(b)) {
                // a <= b
                if (b.lte(c)) {
                    // a <= b, b <= c ->  a <= c
                    assertThat(a.lte(c)).withFailMessage("%s <= %s and %s <= %s -> %s <= %s", a, b, b, c, a, c).isTrue();
                } else {
                    getLogger().debug("a <= b,  c <=  b");
                }
            } else {
                // b < a
                if (a.lte(c)) {
                    // b < a, a < c
                    assertThat(b.lt(c)).withFailMessage("%s < %s and %s < %s -> %s < %s", b, a, a, c, b, c).isTrue();
                } else {
                    // b < a, a > c
                    getLogger().debug("b > a,  a < c");
                }
            }
        } catch (NotComparableException ncp) {
            Assume.that(false);
        }
    }


}
