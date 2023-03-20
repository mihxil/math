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
package org.meeuw.theories.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.Sizeable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface SizeableTheory< E extends Sizeable<SIZE>, SIZE extends Scalar<SIZE>> extends ElementTheory<E> {

  @Property
    default void abs(@ForAll(ELEMENTS) E scalar) {
        SIZE abs = scalar.abs();
        getLogger().debug("abs({}) = {}", scalar, abs);
        assertThat(abs.isNegative()).withFailMessage(() -> "abs(" + scalar  + ") = " + abs + " is negative").isFalse();
        assertThat(abs.floatValue()).isGreaterThanOrEqualTo(0.0f);
  }
}
