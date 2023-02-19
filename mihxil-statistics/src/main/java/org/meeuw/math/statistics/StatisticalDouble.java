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
package org.meeuw.math.statistics;

import java.util.Optional;
import java.util.OptionalDouble;

import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * Primitive {@code double} version of {@link StatisticalNumber}.
 */
public interface StatisticalDouble<SELF extends StatisticalDouble<SELF>>
    extends
    UncertainDouble<UncertainReal>,
    StatisticalNumber<SELF, Double, UncertainDoubleElement>, UncertainReal {

    double doubleStandardDeviation();

    default double doubleMean() {
        return optionalDoubleMean()
            .orElseThrow(() ->
                new DivisionByZeroException("No values entered, cannot calculate mean")
            );
    }

    OptionalDouble optionalDoubleMean();

    @Override
    default Optional<Double> getOptionalMean() {
        OptionalDouble optionalDouble = optionalDoubleMean();
        if (optionalDouble.isPresent()) {
            return Optional.of(optionalDouble.getAsDouble());
        } else {
            return Optional.empty();
        }
    }

    @Override
    default Double getStandardDeviation() {
        return doubleStandardDeviation();
    }

    @Override
    default Double getValue() {
        return doubleValue();
    }

    @Override
    default Double getMean() {
        return doubleMean();
    }

    /**
     * The uncertainty of a {@link StatisticalNumber} is its {@link #getStandardDeviation()}, unless {@link Double#isNaN(double)}, in that case it's {@code abs(}{@link #getValue()}{@code )}
     */
    @Override
    default double doubleUncertainty() {
        double uc = doubleStandardDeviation();
        if (Double.isNaN(uc)) {
            // Probably just one value entered. A conservative estimation of the uncertainty is the value itself.
            return Math.abs(getValue());
        } else {
            return uc;
        }
    }

    @Override
    default Double getUncertainty(){
        return doubleUncertainty();
    }

}
