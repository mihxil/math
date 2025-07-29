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
package org.meeuw.math.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormatProvider extends AlgebraicElementFormatProvider<UncertainDoubleFormat> {

    public UncertainDoubleFormatProvider() {

    }

    @Override
    public UncertainDoubleFormat getInstance(Configuration configuration) {
        UncertainDoubleFormat format = new UncertainDoubleFormat();
        NumberConfiguration numberConfiguration = getConfigurationAspect(NumberConfiguration.class);
        format.setMinimumExponent(numberConfiguration.getMinimalExponent());
        format.setNumberFormat(numberConfiguration.getNumberFormat());
        format.setUncertaintyNotation(getConfigurationAspect(UncertaintyConfiguration.class).getNotation());
        format.setConsiderRoundingErrorFactor(getConfigurationAspect(UncertaintyConfiguration.class).getConsiderRoundingErrorFactor());

        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        return UncertainDouble.class.isAssignableFrom(element) ? 1 : -1;
    }
}
