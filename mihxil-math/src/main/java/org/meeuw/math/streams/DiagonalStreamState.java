package org.meeuw.math.streams;

import lombok.Getter;

import java.util.Iterator;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Helper class for {@link StreamUtils#diagonalStream(Supplier, Supplier, BiFunction)}.
 */
class DiagonalStreamState<E1, E2, F> {
    final long size;
    final Function<Long, Stream<E1>> v1;
    final Supplier<Stream<E2>> v2;
    final Iterator<E1> ia;
    final Iterator<E2> ib;

    @Getter
    final E1 a;
    @Getter
    final E2 b;

    DiagonalStreamState(long size, Function<Long, Stream<E1>> v1, Supplier<Stream<E2>> v2) {
        this.size = size;
        this.v1 = v1;
        this.v2 = v2;
        this.ia = v1.apply(size).limit(size).iterator();
        this.ib = v2.get().limit(size).iterator();
        this.a = ia.next();
        this.b = ib.next();
    }

    private DiagonalStreamState(long size, Function<Long, Stream<E1>> v1, Supplier<Stream<E2>> v2, Iterator<E1> ia, Iterator<E2> ib, E1 a, E2 b) {
        this.size = size;
        this.v1 = v1;
        this.v2 = v2;
        this.ia = ia;
        this.ib = ib;
        this.a = a;
        this.b = b;
    }

    public DiagonalStreamState<E1, E2, F> next() {
        if (!ia.hasNext()) {
            return new DiagonalStreamState<>(size + 1, v1, v2);
        } else {
            return copy(ia.next(), ib.next());
        }
    }

    private DiagonalStreamState<E1, E2, F> copy(E1 a, E2 b) {
        return new DiagonalStreamState<>(size, v1, v2, ia, ib, a, b);
    }
}
