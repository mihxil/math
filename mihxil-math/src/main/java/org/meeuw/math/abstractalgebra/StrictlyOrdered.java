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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.exceptions.NotComparableException;

/**
 *
 * Provides operator {@link #lt} and {@link #gt} and guarantees that these are transitive.
 * <p>
 * {@link #lte} and {@link #gte} are provided too, but not necessarily transitively.
 * <p>
 * This implies {@link Comparable}
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 * @param <E> Self reference
 */
public interface StrictlyOrdered<E extends StrictlyOrdered<E>>
    extends Comparable<E> {

    default boolean lt(E compare) throws NotComparableException {
        return compareTo(compare) < 0 && ! this.equals(compare);
    }

    default boolean gt(E compare) throws NotComparableException {
        return compareTo(compare) > 0 && ! this.equals(compare);
    }

    default boolean lte(E compare) throws NotComparableException {
        return lt(compare) || equals(compare);
    }

    default boolean gte(E compare) throws NotComparableException {
        return gt(compare) || equals(compare);
    }

    @Override
    int compareTo(@NonNull E o) throws NotComparableException;
}
