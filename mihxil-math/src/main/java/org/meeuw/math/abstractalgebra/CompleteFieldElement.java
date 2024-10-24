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
import org.meeuw.math.exceptions.*;

/**
 * Elements of a {@link CompleteField}.
 *
 * @see CompleteField
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> Self reference
 */
public interface CompleteFieldElement<E extends CompleteFieldElement<E>>
    extends
    FieldElement<E> {

    @Override
    CompleteField<E> getStructure();

    E sqrt();

    E sin();

    E cos();

    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME, value="Can't be taken of 0 for negative arguments")
    default E pow(E exponent) throws IllegalPowerException, OverflowException {
        try {
            return (ln().times(exponent)).exp();
        } catch (IllegalLogarithmException illegalLogarithmException) {
            throw new IllegalPowerException(illegalLogarithmException);
        }
    }

    default E tetrate(int n) {
        if (n < 0) {
            throw new IllegalPowerException("");
        }
        if (n == 0) {
            return getStructure().one();
        }
        final E t = (E) this;
        E e = t;
        while (--n > 0)  {
            e = t.pow(e);
        }
        return e;
    }

    E exp();

    E ln() throws IllegalLogarithmException;

    default E sinh() {
        return exp().minus(negation().exp()).dividedBy(2);
    }
    default E cosh() {
        return exp().plus(negation().exp()).dividedBy(2);
    }

}
