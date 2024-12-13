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
package org.meeuw.math.streams;

import lombok.*;

import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.IntegerUtils;

import static java.math.BigInteger.ZERO;

/**
 * Utilities related to streams
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    private static final int DEFAULT_MAX_THREADS = 4;

    public static int getMaxThreads() {
        return ConfigurationService.getConfiguration().getAspect(Configuration.class).getMaxThreads();
    }

    public static Stream<BigInteger> bigIntegerStream(boolean includeNegatives) {
        return bigIntegerStream(ZERO, includeNegatives);
    }

    public static Stream<BigInteger> bigIntegerStream(BigInteger start, final boolean includeNegatives) {
        return StreamSupport.stream(
            BigIntegerSpliterator.builder()
                .start(start)
                .includeNegatives(includeNegatives).build(),
            false);
    }

    public static Stream<BigInteger> reverseBigIntegerStream(BigInteger start, final boolean includeNegatives) {
        return StreamSupport.stream(
            BigIntegerSpliterator.builder()
                .start(start)
                .includeNegatives(includeNegatives)
                .step(IntegerUtils.MINUS_ONE)
                .build(),
            false);
    }

    /**
     * Contains the logic to combine two streams.
     * They are found by tracing diagonals in the plane spanned by the two streams.
     *
     * @param stream1 A function to create new stream, which returns all values from the nth value down to the first
     * @param stream2 A supplier to create a new stream
     * @param combiner A bifunction to combine the two values supplied by the two stream to one new value
     * @param <E1> type of the elements of the first stream
     * @param <E2> type of the elements of the second stream
     * @param <F> type of the elements of the resulting stream
     * @return a new stream created using the two given stream suppliers
     *
     * @see #cartesianStream(Supplier[])
     */
    public static <E1, E2, F> Stream<F> diagonalStream(
        Function<Long, Stream<E1>> stream1,
        Supplier<Stream<E2>> stream2,
        BiFunction<E1, E2, F> combiner) {
        return Stream.iterate(new DiagonalStreamState<E1, E2, F>(1L, stream1, stream2), DiagonalStreamState::next)
            .map(s -> combiner.apply(s.getA(), s.getB()));
    }

    /**
     * Defaulting version of {@link #diagonalStream(Function, Supplier, BiFunction)}.
     * <p>
     *
     * @param stream1 The first stream, which will be reversed using {@link #reverseStream(Stream, long)}
     * @param stream2 The second stream
     * @param combiner The function which produces elements of the returned stream using elements from the two given streams
     * @param <E1> type of the elements of the first stream
     * @param <E2> type of the elements of the second stream
     * @param <F> type of the elements of the resulting stream
     * @return  a new stream created using the two given stream suppliers
     * @see #diagonalStream(Function, Supplier, BiFunction)
     */
    public static <E1, E2, F> Stream<F>  diagonalStream(
        Supplier<Stream<E1>> stream1,
        Supplier<Stream<E2>> stream2,
        BiFunction<E1, E2, F> combiner) {
        return diagonalStream(
            (size) -> reverseStream(stream1.get(), size), stream2, combiner);
    }

    /**
     * Reverses a stream. This happens by collecting it to a list first, and then stream that backwards.
     * @param <E> the type of the stream elements
     * @param stream the stream to reverse
     * @param start the index of this stream that will be the first element of the reversed stream
     */
    public static <E> Stream<E> reverseStream(Stream<E> stream, long start) {
        List<E> collect = stream.limit(start).collect(Collectors.toList());
        Collections.reverse(collect);
        return collect.stream();
    }

    /**
     * Returns an infinite stream of all possible integer arrays of given length
     * <p>
     * It will start with an array with only zeros. Then it will return an array filled with all possible combinations of {@code -1, 0, 1}, then with all possibles arrays with only {@code -2, -1, 0, 1, 2} (and it will not produce any entries which already occurred). And so on.
     *
     * @param length the length of all arrays to produce
     * @return an infinite stream of integer arrays of given length
     */
    public static Stream<int[]> allIntArrayStream(int length) {
        return Stream
            .iterate(new IntArrayState(length), IntArrayState::next)
            .map(IntArrayState::array);
    }

    /**
     * Returns a stream of all possible combinations of values of multiple streams. (A 'cartesian product').
     * <p>
     * The streams may be infinite.
     * <p>
     * The produced result will be an array of all first elements of the streams. Then it will produce entries with the first and second elements (skipping the ones with only the first), and so on.
     * <p>
     * This way every finite combination will eventually occur.
     *
     * @return a stream of arrays
     *
     */
    @SuppressWarnings("unchecked")
    public static <E> Stream<E[]> cartesianStream(final Supplier<Stream<? extends E>>... streams) {
        return cartesianStream(Arrays.asList(streams));
    }
    public static <E> Stream<E[]> cartesianStream(final List<Supplier<Stream<? extends E>>> streams) {

        return StreamSupport.stream(
            new CartesianSpliterator<>(
                streams.stream()
                    .map(StreamUtils::spliterator)
                    .toArray(Supplier[]::new)
        ), false);
    }

    @NonNull
    public static <E> Stream<E[]> nCartesianStream(int count, @NonNull final Supplier<Stream<? extends E>> stream) {
        return StreamSupport.stream(new CartesianSpliterator<E>(spliterator(stream), count), false);
    }

    private static <E> Supplier<Spliterator<? extends E>> spliterator(Supplier<Stream<? extends E>> stream) {
        return () -> stream.get().spliterator();
    }


    /**
     * Fills the array as if it is kind of a number system (with infinite base, but fixed number of digits)
     * Also we fill from the left.
     * <p>
     * The idea is to first produce values with low base, and if we filled those, then increase the base with 2 (we want to produce
     * negative values, and afterwards shift base /2).
     * <p>
     * This will produce duplicates, which are filtered out in {@link IntArrayState}
     *
     */
    public static int inc(int[] counters, int base) {
        return inc(counters, 0, base);
    }

    private static int inc(int[] counters, int index, int base) {
        counters[index]++;
        if (counters[index] > base) {
            counters[index] = 0;
            if (index + 1 < counters.length) {
                return inc(counters, index + 1, base);
            } else {
                counters[0] = base;
                return inc(counters, 0, base + 2);
            }
        }
        return base;
    }

    @ToString
    @EqualsAndHashCode
    public static class Configuration implements ConfigurationAspect {

        @With
        @Getter
        private final int maxThreads;

        public Configuration() {
            this(DEFAULT_MAX_THREADS);
        }


        @lombok.Builder
        public Configuration(int maxThreads) {
            this.maxThreads = maxThreads == -1 ? DEFAULT_MAX_THREADS : maxThreads;
        }

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.singletonList(StreamUtils.class);
        }
    }

}
