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
package org.meeuw.test.math.streams;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.*;

import org.junit.jupiter.api.Test;

import org.meeuw.math.streams.CartesianSpliterator;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class CartesianSpliteratorTest {

    /**
     * The edge case of no spliterator.
     *
     */
    @Test
    public void zero() {
         CartesianSpliterator<String> cartesianSpliterator =
            new CartesianSpliterator<>(String.class);

        assertThat(cartesianSpliterator.estimateSize()).isEqualTo(0L);
        testEnd(cartesianSpliterator);

        assertThat(cartesianSpliterator.getElementClass()).isEqualTo(String.class);
    }

    /**
     * The edge case of only one spliterator.
     *
     * This should simply be exactly like this iterator itself.
     */
    @Test
    public void one() {
        final List<String> values = Arrays.asList("a", "b", "c");

        CartesianSpliterator<String> cartesianSpliterator =
            new CartesianSpliterator<>(values::spliterator, 1);

        assertThat(cartesianSpliterator.estimateSize()).isEqualTo(3L);
        testAdvance(cartesianSpliterator, "a");
        testAdvance(cartesianSpliterator, "b");
        testAdvance(cartesianSpliterator, "c");
        testEnd(cartesianSpliterator);
    }



    @Test
    public void twoWithEmpty() {
        final List<String> values1 = Arrays.asList("a", "b", "c");
        final List<String> values2 = Collections.emptyList();

        CartesianSpliterator<String> cartesianSpliterator =
            new CartesianSpliterator<>(values1::spliterator, values2::spliterator);

        assertThat(cartesianSpliterator.estimateSize()).isEqualTo(0L);
        testEnd(cartesianSpliterator);
    }

    @Test
    public void two() {
        final List<String> values = Arrays.asList("a", "b", "c");

        CartesianSpliterator<String> cartesianSpliterator =
            new CartesianSpliterator<>(values::spliterator, 2);

        assertThat(cartesianSpliterator.estimateSize()).isEqualTo(9L);

        testAdvance(cartesianSpliterator, "a", "a");
        testAdvance(cartesianSpliterator, "b", "a");
        testAdvance(cartesianSpliterator, "b", "b");
        testAdvance(cartesianSpliterator, "a", "b");
        testAdvance(cartesianSpliterator, "c", "a");
        testAdvance(cartesianSpliterator, "c", "b");
        testAdvance(cartesianSpliterator, "c", "c");
        testAdvance(cartesianSpliterator, "a", "c");
        testAdvance(cartesianSpliterator, "b", "c");
        testEnd(cartesianSpliterator);
    }

    @SuppressWarnings("UnusedReturnValue")
    @SafeVarargs
    private <E> E[] testAdvance(CartesianSpliterator<? extends E> cartesianSpliterator, E... values) {
        return testAdvance(cartesianSpliterator, i ->
            log.info("Found {}", cartesianSpliterator.currentAsString()),
            values
        );
    }


    @SafeVarargs
    private <E> E[] testAdvance(CartesianSpliterator<? extends E> cartesianSpliterator, Consumer<CartesianSpliterator<? extends E>> consumer, E... values) {
        final List<E[]> container = new ArrayList<>();
        assertThat(cartesianSpliterator.tryAdvance((a) -> {
            final E[] e = a;
            container.add(e);
            assertThat(e).containsExactly(values);
        })).isTrue();
        return container.get(0);
    }

    private <E> void testEnd(CartesianSpliterator<E> cartesianSpliterator) {
        assertThat(cartesianSpliterator.tryAdvance((a) -> {})).isFalse();
    }


    @Test
    public void twoDifferentSizesAndTypes() {
        final List<String> values1 = Arrays.asList("a", "b", "c");
        final List<Integer> values2 = Arrays.asList(1, 2);

        final CartesianSpliterator<?> cartesianSpliterator =
            new CartesianSpliterator<>(values1::spliterator, values2::spliterator);

        assertThat(cartesianSpliterator.estimateSize()).isEqualTo(6L);
        assertThat(cartesianSpliterator.getElementClass()).isEqualTo(Serializable.class);


        testAdvance(cartesianSpliterator, "a", 1);
        assertThat(cartesianSpliterator.currentAsString()).isEqualTo("a̳, 1̲₀ (0)");
        testAdvance(cartesianSpliterator, "b", 1);
        testAdvance(cartesianSpliterator, "b", 2);
        testAdvance(cartesianSpliterator, "a", 2);
        testAdvance(cartesianSpliterator, "c", 1);
        testAdvance(cartesianSpliterator, "c", 2);
        testEnd(cartesianSpliterator);
    }

    @Test
    public void three() {
        final List<Integer> values = Arrays.asList(1, 2, 3);

        CartesianSpliterator<Integer> cartesianSpliterator =
            new CartesianSpliterator<>(values::spliterator, 3);

//        assertThat(cartesianSpliterator.estimateSize()).isEqualTo(27L);

        // fix 0 (1)
        testAdvance(cartesianSpliterator, 1, 1, 1);

        // fix 0 (4)
        testAdvance(cartesianSpliterator, 2, 1, 1);
        testAdvance(cartesianSpliterator, 2, 2, 1);
        testAdvance(cartesianSpliterator, 2, 1, 2);
        testAdvance(cartesianSpliterator, 2, 2, 2);

        // fix 1 (2)
        testAdvance(cartesianSpliterator,1, 2, 1);
        testAdvance(cartesianSpliterator,1, 2, 2);
        // fix 3 (1)
        testAdvance(cartesianSpliterator,1, 1, 2);

         // fix 0 (9)
        testAdvance(cartesianSpliterator, 3, 1, 1);
        testAdvance(cartesianSpliterator, 3, 2, 1);
        testAdvance(cartesianSpliterator, 3, 3, 1);
        testAdvance(cartesianSpliterator, 3, 1, 2);
        testAdvance(cartesianSpliterator, 3, 2, 2);
        testAdvance(cartesianSpliterator, 3, 3, 2);
        testAdvance(cartesianSpliterator, 3, 1, 3);
        testAdvance(cartesianSpliterator, 3, 2, 3);
        testAdvance(cartesianSpliterator, 3, 3, 3);

        // fix 2 (6)
        testAdvance(cartesianSpliterator, 1, 3, 1);
        testAdvance(cartesianSpliterator, 2, 3, 1);
        testAdvance(cartesianSpliterator, 1, 3, 2);
        testAdvance(cartesianSpliterator, 2, 3, 2);
        testAdvance(cartesianSpliterator, 1, 3, 3);
        testAdvance(cartesianSpliterator, 2, 3, 3);

          // fix 3 (4)
        testAdvance(cartesianSpliterator, 1, 1, 3);
        testAdvance(cartesianSpliterator, 2, 1, 3);
        testAdvance(cartesianSpliterator, 1, 2, 3);
        testAdvance(cartesianSpliterator, 2, 2, 3);

        testEnd(cartesianSpliterator);
    }

    @Test
    public void infiniteStreams() {
        Supplier<Spliterator<? extends Integer>> iterate = () -> Stream.iterate(1, i -> i + 2).spliterator();
        CartesianSpliterator<Integer> cartesianSpliterator =
            new CartesianSpliterator<>(iterate, 3);
        final List<Integer[]> result = new ArrayList<>();
        StreamSupport.stream(cartesianSpliterator, false).limit(100).forEach(a -> {
            log.info("{}", Arrays.asList(a));
            result.add(a);
            }
        );
        assertThat(result).doesNotHaveDuplicates();
    }

    @Test
    public void infiniteStreams2() throws IOException {

        File dest = new File(System.getProperty("user.dir"), "../docs/positive-plane.data");
        try (PrintWriter printer = new PrintWriter(new FileOutputStream(dest))) {
            Supplier<Spliterator<? extends Integer>> positive = () -> Stream.iterate(0, i -> i + 1).spliterator();
            CartesianSpliterator<Integer> cartesianSpliterator =
                new CartesianSpliterator<>(positive, 2);
            StreamSupport.stream(cartesianSpliterator, false).limit(1000).forEach(a ->
                printer.println(
                    cartesianSpliterator.getIndex() + " " +
                        Stream.of(a).map(String::valueOf).collect(Collectors.joining(" "))
                )
            );
        }
    }


    @Test
    public void infiniteStreams3() throws IOException {

        File dest = new File(System.getProperty("user.dir"), "../docs/positive-3-plane.data");
        try (PrintWriter printer = new PrintWriter(new FileOutputStream(dest))) {
            Supplier<Spliterator<? extends Integer>> positive = () -> Stream.iterate(0, i -> i + 1).spliterator();
            CartesianSpliterator<Integer> cartesianSpliterator =
                new CartesianSpliterator<>(positive, 3);
            StreamSupport.stream(cartesianSpliterator, false).limit(1000).forEach(a ->
                printer.println(
                    cartesianSpliterator.getIndex() + " " +
                        Stream.of(a).map(String::valueOf).collect(Collectors.joining(" "))
                )
            );
        }
    }

}
