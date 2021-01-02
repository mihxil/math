package org.meeuw.math;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
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
    }

    @Test
    public void bigIntegerStream() {
        Stream<BigInteger> stream = Streams.bigIntegerStream(true);
        stream.limit(5000).forEach(i -> {
            log.info(i);
        });
    }

    @Test
    public void bigPositiveIntegerStream() {
        Stream<BigInteger> stream = Streams.bigIntegerStream(false);
        assertThat(stream.limit(20))
            .startsWith(valueOf(0), valueOf(1), valueOf(2), valueOf(3), valueOf(4));
    }

    @Test
    public void diagonalStream() {
        Stream<A> aStream = Streams.diagonalStream(
            () -> Streams.bigIntegerStream(true),
            () -> Streams.bigIntegerStream(false), A::new);
        aStream.limit(100).forEach(i -> {
            log.info(i);
        });

    }
}
