package org.meeuw.math;

import lombok.Getter;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Streams {

    public static final int MAX_THREADS = 4;

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
                .step(BigInteger.valueOf(-1L))
                .build(),
            false);
    }

    public static <E, F> Stream<F> diagonalStream(
        Function<Long, Stream<E>> stream1, Supplier<Stream<E>> stream2, BiFunction<E, E, F> combiner) {
        return Stream.iterate(new State<E, F>(1L, stream1, stream2), State::next)
            .map(s -> combiner.apply(s.getA(), s.getB()));
    }

    public static <E, F> Stream<F> diagonalStream(
        Supplier<Stream<E>> stream1, Supplier<Stream<E>> stream2, BiFunction<E, E, F> combiner) {
        return diagonalStream(
            (size) -> reverseStream(stream1.get(), size), stream2, combiner);
    }

    public static <E> Stream<E> reverseStream(Stream<E> stream, long start) {
        List<E> collect = stream.limit(start).collect(Collectors.toList());
        Collections.reverse(collect);
        return collect.stream();
    }


    static class BigIntegerSpliterator implements Spliterator<BigInteger> {
        private BigInteger current;
        private boolean negatives;
        private BigInteger step;
        private boolean acceptNegative;
        private final BigInteger stepSignum;
        private final AtomicInteger thread;

        @lombok.Builder
        private BigIntegerSpliterator(BigInteger start, boolean includeNegatives, BigInteger step, AtomicInteger thread) {
            this.current = start;
            this.negatives = includeNegatives;
            this.step = step == null ? ONE : step;
            this.stepSignum = BigInteger.valueOf(this.step.signum());
            this.acceptNegative = false;
            this.thread = thread == null ? new AtomicInteger(1) : thread;
        }
        @lombok.Builder
        public BigIntegerSpliterator(BigInteger start, boolean includeNegatives, BigInteger step) {
            this(start, includeNegatives, step, null);
         }

        protected BigIntegerSpliterator copy() {
            BigIntegerSpliterator c = new BigIntegerSpliterator(current, negatives, step, thread);
            c.acceptNegative = acceptNegative;
            return c;
        }

        protected void accept(Consumer<? super BigInteger> action) {
            if (acceptNegative) {
                action.accept(current.negate().multiply(stepSignum));
            } else {
                action.accept(current.multiply(stepSignum));
            }
        }
        protected void advance() {
            if (negatives) {
                if (current.intValue() > 0 && ! acceptNegative) {
                    acceptNegative = true;
                } else {
                    acceptNegative = false;
                    current = current.add(step);
                }
            } else {
                current = current.add(step);
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super BigInteger> action) {
            accept(action);
            advance();
            return true;
        }

        @Override
        public BigIntegerSpliterator trySplit() {
            if (thread.get() >= MAX_THREADS) {
                return null;
            }
            return _trySplit();

        }

        BigIntegerSpliterator _trySplit() {
            thread.incrementAndGet();
            if (negatives) {
                negatives = false;
                BigIntegerSpliterator otherStream = copy();
                otherStream.acceptNegative = true;
                if (current.intValue() == 0) {
                    otherStream.advance();
                }
                return otherStream;
            } else {
                BigInteger prevStep = step;
                step = step.multiply(BigInteger.valueOf(2));
                BigIntegerSpliterator otherStream = copy();
                otherStream.current = otherStream.current.add(prevStep);
                return otherStream;
            }
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics() {
            return DISTINCT | NONNULL | IMMUTABLE;
        }

    }

    /**
     *  Helper class for {@link #diagonalStream(Supplier, Supplier, BiFunction)} ()}. Contains the logic to combine two stream.
     * They are found by tracing diagonals in the plain spanned by the two stream.
     */
    private static class State<E, F>  {
        final long size;
        final Function<Long, Stream<E>> v1;
        final Supplier<Stream<E>> v2;
        final Iterator<E> ia;
        final Iterator<E> ib;

        @Getter
        final E a;
        @Getter
        final E b;

        private State(long size, Function<Long, Stream<E>> v1, Supplier<Stream<E>> v2) {
            this.size = size;
            this.v1 = v1;
            this.v2 = v2;
            this.ia = v1.apply(size).limit(size).iterator();
            this.ib = v2.get().limit(size).iterator();
            this.a = ia.next();
            this.b = ib.next();
        }

        private State(long size,  Function<Long, Stream<E>> v1, Supplier<Stream<E>> v2, Iterator<E> ia, Iterator<E> ib, E a, E b) {
            this.size = size;
            this.v1 = v1;
            this.v2 = v2;
            this.ia = ia;
            this.ib = ib;
            this.a = a;
            this.b = b;
        }

        public State<E, F> next() {
            if (! ia.hasNext()) {
                return new State<>(size + 1, v1, v2);
            } else {
                return copy(ia.next(), ib.next());
            }
        }

        private State<E, F> copy(E a, E b) {
            return new State<>(size, v1, v2, ia, ib, a, b);
        }
    }
}