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
package org.meeuw.test.math.abstractalgebra.integers;

import java.util.stream.Collectors;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.integers.Square;
import org.meeuw.math.abstractalgebra.integers.Squares;
import org.meeuw.theories.abstractalgebra.MultiplicativeAbelianSemiGroupTheory;
import org.meeuw.theories.numbers.SizeableScalarTheory;

import static org.meeuw.math.IntegerUtils.MAX_SQUARABLE;

/**
 * @author Michiel Meeuwissen
 * @since 0.18
 */
class SquareTest implements
    MultiplicativeAbelianSemiGroupTheory<Square>,
    SizeableScalarTheory<Square, Square> {



    @Override
    public Arbitrary<Square> elements() {
        return Arbitraries.randomValue((random) -> {

            var l = Math.abs(random.nextLong()) % (MAX_SQUARABLE + 1); // ensure positive
            return Square.of(l * l); // ensure even
        });
    }

    @Test
    void stream() {
        Assertions.assertThat(Squares.INSTANCE.stream().limit(11).map(Square::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 1L, 4L, 9L, 16L, 25L, 36L, 49L, 64L, 81L, 100L);
    }

}
