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
package org.meeuw.math.abstractalgebra;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * This interface is missing in java. We use it to mark an {@link AlgebraicStructure} as 'countable'. And the (possibly infinite)  {@link #stream()} method would actually do it.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <T> the type of the stream elements
 */
public interface Streamable<T> extends Iterable<T> {

    /**
     * @throws org.meeuw.math.exceptions.NotStreamable if not streamable after all
     */
    Stream<T> stream();

    /**
     * Sometimes it is useful to be able to produce an 'reversed' stream.
     * This default implementation just bases it on {@link #stream()}, but it may be implementable more efficiently.
     * @param first the sequence number of the first element to return in the reverse stream.
     * @return a stream streaming in the other direction
     */
    default Stream<T> reverseStream(long first) {
        List<T> s = stream().limit(first).collect(Collectors.toList());
        Collections.reverse(s);
        return s.stream();
    }

    @NonNull
    @Override
    default Iterator<T> iterator() {
        return stream().iterator();
    }

}
