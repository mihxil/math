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
package org.meeuw.math.uncertainnumbers.field;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.UncertainScalar;

/**
 * An element of the {@link UncertainRealField}.
 * This is the connection between {@link org.meeuw.math.uncertainnumbers.UncertainNumber} and {@link org.meeuw.math.abstractalgebra.FieldElement}s.
 * <p>
 * So, an uncertain scalar that is also an element of an algebra (a {@link org.meeuw.math.abstractalgebra.Field}, event)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainReal
    extends
    UncertainScalar<Double, UncertainReal>,
    UncertainDouble<UncertainReal>,
    CompleteScalarFieldElement<UncertainReal> {

    @Override
    UncertainReal negation();

    @Override
    default UncertainReal times(UncertainReal multiplier) {
        return UncertainDouble.super.times(multiplier);
    }

    @Override
    UncertainReal pow(int n);

    @Override
    default UncertainReal plus(UncertainReal summand) {
        return UncertainDouble.super.plus(summand);
    }


    /**
     * For uncertain elements, an element is only zero if its value is {@link #isExact()}
     * and of course {@code 0}.
     */
    @Override
    default boolean isZero() {
        return isExact() && CompleteScalarFieldElement.super.isZero();
    }


    /**
     * For uncertain elements, an element is only one if its value is {@link #isExact()}
     * and of course {@code 1}.
     */
    @Override
    default boolean isOne() {
        return isExact() && CompleteScalarFieldElement.super.isOne();
    }

}
