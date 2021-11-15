package org.meeuw.math.streams;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartesianSpliteratorTest {

    @Test
    public void basic() {
        List<Integer> values = Arrays.asList(1, 2, 3);

        CartesianSpliterator<Integer> cartesianSpliterator =
            new CartesianSpliterator<>(values::spliterator, 3);

        List<Integer[]> result = new ArrayList<>();
        cartesianSpliterator.forEachRemaining(result::add);
        assertThat(result).containsExactly(
            of(1, 1, 1),
            of(2, 1, 1),
            of(1, 2, 1),
            of(2, 2, 1),
            of(1, 1, 2),
            of(2, 1, 2),
            of(2, 2, 1),
            of(2, 2, 2),

            of(3, 1, 1),
            of(3, 2, 1),
            of(3, 3, 1),
            of(3, 1, 2),
            of(3, 2, 2),
            of(3, 3, 2),

            of(3, 3, 3),

            of(1, 3, 1),
            of(2, 3, 1)
        );

    }

    <E> E[] of(E... values) {
        return values;
    }

}
