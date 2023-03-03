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
package org.meeuw.math;

/**
 * Some structures can naturally be {@link #times(double)  multiplied} by (and {@link #dividedBy(double) divided by}) a {@code double}.
 *
 * @author Michiel Meeuwissen
 * @since 0.9
 * @param <E> Self reference
 * @see WithScalarOperations
 */
public interface WithDoubleOperations<E> {

    /**
     * Multiplies the current object with a float and returns a new instance
     * @param multiplier a scalar to multiply with
     * @return this * multiplier
     */
    E times(double multiplier);

    /**
     * Divides the current object with a scalar and returns a new instance
     * @param divisor a scalar to divide by
     * @return this / divisor
     */
    default E dividedBy(double divisor) {
        return times(1d/divisor);
    }

}
