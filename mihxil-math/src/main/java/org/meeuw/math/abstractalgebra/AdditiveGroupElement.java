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

/**
 * An element for the algebraic 'group' (where the operation is addition)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupElement<E extends AdditiveGroupElement<E>>
    extends AdditiveMonoidElement<E>, GroupElement<E> {

    @Override
    AdditiveGroup<E> getStructure();

    /**
     * @return the additive inverse of this element
     */
    E negation();

    default E minus(E subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    default E inverse() {
        return negation();
    }

    /**
     * If addition is defined, then you can also have 'repeated' addition. This is a bit, but not quite like {@link MultiplicativeGroupElement#times(MultiplicativeSemiGroupElement)}
     *
     * It's actually also more or less similar to {@link MultiplicativeGroupElement#pow(int)}
     * @param multiplier the number of times this element is to be added to itself
     * @return this * multiplier
     */
    @SuppressWarnings("unchecked")
    default E repeatedPlus(int multiplier) {
        if (multiplier == 0) {
            return getStructure().zero();
        }
        int m = Math.abs(multiplier);
        E self = (E) this;
        E result = self;
        while (--m > 0) {
            result = result.plus(self);
        }
        if (multiplier < 0) {
            return result.negation();
        } else {
            return result;
        }
    }

}
