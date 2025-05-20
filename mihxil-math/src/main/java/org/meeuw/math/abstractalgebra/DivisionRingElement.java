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

import org.meeuw.math.NonAlgebraic;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRingElement<E extends DivisionRingElement<E>> extends
    MultiplicativeGroupElement<E>,
    RingElement<E> {

    @Override
    DivisionRing<E> getStructure();

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation uses the {@link DivisionRing#groupOperator()}  group operator} of {@link #getStructure() the structure}.
     * @param operand
     * @return
     */
    @Override
    default E operate(E operand) {
        return getStructure().groupOperator().apply(self(), operand);
    }
    @Override
    @NonAlgebraic
    default E inverse() {
        return getStructure().groupOperator().inverse(self());
    }

    @Override
    @NonAlgebraic
    default E dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }


}
