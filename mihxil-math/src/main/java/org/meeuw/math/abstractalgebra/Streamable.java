package org.meeuw.math.abstractalgebra;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This interface is missing in java. We use it to mark an {@link AlgebraicStructure} as 'countable'. And the (possibly infinite)  {@link #stream()} method would actually do it.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Streamable<T> {

    Stream<T> stream();

    /**
     * Sometimes it is usefull to be able to produce an 'reversed' stream.
     * This default implementation just bases it on {@link #stream()}, but it may be implementable more efficiently.
     */
    default Stream<T> reverseStream(long first) {
        List<T> s = stream().limit(first).collect(Collectors.toList());
        Collections.reverse(s);
        return s.stream();
    }

}
