package org.meeuw.math.abstractalgebra;

import java.util.stream.Stream;

/**
 * This interface is missing in java. We use it to mark an {@link AlgebraicStructure} as 'countable'. And the (possibly infinite)  {@link #stream()} method would actually do it.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Streamable<T> {

    Stream<T> stream();
}
