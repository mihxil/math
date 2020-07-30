package org.meeuw.math.abstractalgebra;

import java.util.stream.Stream;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Streamable<T> {

    Stream<T> stream();
}
