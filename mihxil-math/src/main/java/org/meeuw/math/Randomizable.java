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

import java.util.Random;

/**
 * {@link org.meeuw.math.abstractalgebra.AlgebraicStructure algebraic structures} can be 'randomizable', and then
 * also provide {@link #nextRandom(Random)} to generate a random element.
 *
 * @see org.meeuw.math.abstractalgebra.AlgebraicStructure
 * @since 0.7
 * @param <E> The returned type of {@link #nextRandom(Random)}. Targeted at algebraic structures, but the type is not restricted to that there, so this interface could be used for other types as well.
 */
public interface Randomizable<E> {

    /**
     * Provides a new random element
     * @param random the random number generator to use
     */
    E nextRandom(Random random);
}
