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
package org.meeuw.math.numbers;

import org.checkerframework.common.value.qual.IntRange;

/**
 * An object with a clearly defined concept of 'negativity'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface SignedNumber<SELF extends SignedNumber<SELF>> extends Comparable<SELF> {

    /**
     * Returns 1 for positive, -1 for negative and 0 for zero.
     * See <a href="https://en.wikipedia.org/wiki/Sign_function">Sign function</a>
     * @return {@code 1, -1} or  {@code 0}
     */
    @IntRange(from = -1, to = 1)
    int signum();

    default boolean isPositive() {
        return signum() > 0;
    }

    default boolean isNegative() {
        return signum() < 0;
    }

    default boolean isZero() {
        return signum() == 0;
    }

}
