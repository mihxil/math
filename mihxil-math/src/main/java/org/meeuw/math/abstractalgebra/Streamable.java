package org.meeuw.math.abstractalgebra;

import java.util.stream.Stream;

/**
 * This interface is missing in java. We use it to mark a {@link AlgebraicStructure} as 'countable', and the ({@link #stream() would actually do it)}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Streamable<T> {

    Stream<T> stream();
}
