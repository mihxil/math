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
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * The operations {@link #reciprocal()}, {@link #dividedBy(MultiplicativeGroupElement)} and {@link #pow(int)} are on default implemented
 * using one of the others.
 *
 * You have to override at least one, to break the stack overflow happening otherwise.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupElement<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoidElement<E>, GroupElement<E> {

    @Override
    MultiplicativeGroup<E> getStructure();

    /**
     * @return the multiplicative inverse
     */
    E reciprocal();

    @Override
    default E inverse() {
        return reciprocal();
    }

    /**
     * if multiplication and division is defined, then so is exponentiation to any integer power.
     *
     * This default implementation is based on the default implementation of {@link MultiplicativeMonoidElement#pow(int)
     */
    @Override
    default E pow(int n) {
        if (n < 0) {
            n *= -1;
            return reciprocal().pow(n);
        }
        if (n == 0) {
            return getStructure().one();
        }
        return MultiplicativeMonoidElement.super.pow(n);
    }

    @NonAlgebraic
    default E dividedBy(E divisor) throws ReciprocalException {
        return times(divisor.reciprocal());
    }

}
