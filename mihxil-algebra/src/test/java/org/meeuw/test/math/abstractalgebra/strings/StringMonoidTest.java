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
package org.meeuw.test.math.abstractalgebra.strings;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.strings.StringElement;
import org.meeuw.math.abstractalgebra.strings.StringMonoid;
import org.meeuw.math.abstractalgebra.test.AdditiveMonoidTheory;
import org.meeuw.math.abstractalgebra.test.OrderedTheory;
import org.meeuw.util.test.CharSequenceTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.strings.StringMonoid.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class StringMonoidTest implements
    AdditiveMonoidTheory<StringElement>,
    OrderedTheory<StringElement>,
    CharSequenceTheory<StringElement> {

    @Test
    public void stream1() {
        Assertions.assertThat(INSTANCE.stream(new StringMonoid.State('a', 'x')).limit(10)).map(StringElement::toString)
            .containsExactly(
                "ax", "ay", "az", "a ", "aª", "aµ", "aº", "aÀ", "aÁ", "aÂ"
            );
    }
    @Test
    public void stream2() {
        assertThat(INSTANCE.stream(new StringMonoid.State('a', StringMonoid.LAST_CHAR - 1)).limit(10)).map(StringElement::toString)
            .containsExactly(
                "a鼻", "a𪘀", "b ", "b0", "b1", "b2", "b3", "b4", "b5", "b6"
            );
    }
    @Test
    public void stream3() {
        assertThat(INSTANCE.stream(new StringMonoid.State('a', StringMonoid.LAST_CHAR, StringMonoid.LAST_CHAR - 1)).limit(10)).map(StringElement::toString)
            .containsExactly(
                "a𪘀鼻",
                "a𪘀𪘀",
                "b𪘀 ",
                "b𪘀0",
                "b𪘀1",
                "b𪘀2",
                "b𪘀3",
                "b𪘀4",
                "b𪘀5",
                "b𪘀6"
            );
    }

    @Override
    @Provide
    public Arbitrary<StringElement> elements() {
        return Arbitraries.of("a", "foo", "bar", "")
            .map(StringElement::new);
    }




}
