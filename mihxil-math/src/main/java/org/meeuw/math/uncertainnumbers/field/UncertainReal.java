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

/**
 * An element of the {@link UncertainRealField}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainReal
    extends
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

    @Override
    default boolean isZero() {
        return isExact() && CompleteScalarFieldElement.super.isZero();
    }

    @Override
    default boolean isOne() {
        return isExact() && CompleteScalarFieldElement.super.isOne();
    }

}
