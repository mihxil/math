package org.meeuw.math;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.Streams.BigIntegerSpliterator;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class StreamsTest {

    @Data
    static class A  {
        final BigInteger a;
        final BigInteger b;

        A(BigInteger a, BigInteger b) {
            this.a = a;
            this.b = b;
        }
        @Override
        public String toString() {
            return a + "," + b;
        }
    }

    @Test
    public void bigIntegerStream() {
        Stream<BigInteger> stream = Streams.bigIntegerStream(true);
        assertThat(stream.limit(11).mapToInt(BigInteger::intValue)).containsExactly(
            0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5
        );
    }
    @Test
    public void reverseBigIntegerStream() {
        Stream<BigInteger> stream = Streams.reverseBigIntegerStream(BigInteger.valueOf(5), true);
        assertThat(stream.limit(11).mapToInt(BigInteger::intValue)).containsExactly(
            -5, 5, -4, 4, -3, 3, -2, 2, -1, 1, 0
        );
    }

    @Test
    public void spliterator() {
        BigIntegerSpliterator i = new BigIntegerSpliterator(BigInteger.valueOf(0), true, BigInteger.ONE);
        Spliterator<BigInteger> negatives = i.trySplit();

        assertThat(StreamSupport.stream(i, false).limit(10).map(BigInteger::intValue)).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertThat(StreamSupport.stream(negatives, false).limit(10).map(BigInteger::intValue)).containsExactly(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10);
    }
    @Test
    public void spliterator2() {
        BigIntegerSpliterator i = new BigIntegerSpliterator(BigInteger.valueOf(0), true, BigInteger.ONE);
        Spliterator<BigInteger> negatives = i.trySplit();
        Spliterator<BigInteger> negativeEvens = negatives.trySplit();
        Spliterator<BigInteger> odds = i.trySplit();


        assertThat(StreamSupport.stream(i, false).limit(10).map(BigInteger::intValue)).containsExactly(0, 2, 4, 6, 8, 10, 12, 14, 16, 18);
        assertThat(StreamSupport.stream(negatives, false).limit(10).map(BigInteger::intValue)).containsExactly(-1, -3, -5, -7, -9, -11, -13, -15, -17, -19);
        assertThat(StreamSupport.stream(odds, false).limit(10).map(BigInteger::intValue)).containsExactly(1, 3, 5, 7, 9, 11, 13, 15, 17, 19);
        assertThat(StreamSupport.stream(negativeEvens, false).limit(10).map(BigInteger::intValue)).containsExactly(-2, -4, -6, -8, -10, -12, -14, -16, -18, -20);
    }

    @Test
    public void spliterator3() {
        BigIntegerSpliterator i = new BigIntegerSpliterator(BigInteger.valueOf(0), true, BigInteger.ONE);
        BigIntegerSpliterator negatives = i._trySplit();
        BigIntegerSpliterator negativeEvens = negatives._trySplit();
        BigIntegerSpliterator odds = i._trySplit();


        Spliterator<BigInteger> four = i._trySplit();
        assertThat(StreamSupport.stream(four, false).limit(10).map(BigInteger::intValue)).containsExactly(2, 6, 10, 14, 18, 22, 26, 30, 34, 38);

        assertThat(StreamSupport.stream(i, false).limit(10).map(BigInteger::intValue)).containsExactly(0, 4, 8, 12, 16, 20, 24, 28, 32, 36);




    }

    @Test
    public void trySplit() throws InterruptedException {
        // https://michaelbespalov.medium.com/parallel-stream-pitfalls-and-how-to-avoid-them-91f11808a16c
        final Set<BigInteger> needed = Stream.concat(Stream.of(ZERO), Stream.iterate(ONE, i -> i.add(ONE)).flatMap(i -> Stream.of(i, i.negate()))).limit(100).collect(Collectors.toCollection(CopyOnWriteArraySet::new));

        ForkJoinPool customThreadPool = new ForkJoinPool(Streams.MAX_THREADS);
        ForkJoinTask<?> submit = customThreadPool.submit(() -> {
            Stream<BigInteger> stream = Streams.bigIntegerStream(true);
            stream.parallel().forEach(i -> {

                if (needed.remove(i)) {
                    synchronized (StreamsTest.this) {
                        StreamsTest.this.notifyAll();
                    }
                }
            });
        });
        synchronized (this) {
            while (! needed.isEmpty()) {
                this.wait();
                log.info("{}", needed);
            }
        }
        customThreadPool.shutdownNow();
    }

    @Test
    public void bigPositiveIntegerStream() {
        Stream<BigInteger> stream = Streams.bigIntegerStream(false);
        assertThat(stream.limit(20).mapToInt(BigInteger::intValue)).containsExactly(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19
        );
    }

    @Test
    public void diagonalStream() {
        Stream<A> aStream = Streams.diagonalStream(
            () -> Streams.bigIntegerStream(false),
            () -> Streams.bigIntegerStream(false), A::new);

        assertThat(aStream.limit(20).map(A::toString)).containsExactly(
            "0,0",
            "1,0",
            "0,1",
            "2,0",
            "1,1",
            "0,2",
            "3,0",
            "2,1",
            "1,2",
            "0,3",
            "4,0",
            "3,1",
            "2,2",
            "1,3",
            "0,4",
            "5,0",
            "4,1",
            "3,2",
            "2,3",
            "1,4");
    }
}
