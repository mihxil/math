/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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
import org.meeuw.math.text.UncertainNumberFormat;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public class UncertainNumberFormatProvider extends AlgebraicElementFormatProvider<UncertainNumberFormat> {

    @Override
    public UncertainNumberFormat getInstance(Configuration configuration) {
        UncertainNumberFormat format = new UncertainNumberFormat();
        NumberConfiguration numberConfiguration = getConfigurationAspect(NumberConfiguration.class);
        format.setMinimumExponent(numberConfiguration.getMinimalExponent());
        format.setNumberFormat(numberConfiguration.getNumberFormat());
        format.setUncertaintyNotation(getConfigurationAspect(UncertaintyConfiguration.class).getNotation());
        format.setConsiderRoundingErrorFactor(getConfigurationAspect(UncertaintyConfiguration.class).getConsiderRoundingErrorFactor());

        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        return UncertainNumber.class.isAssignableFrom(element) ? 1 : 0;
    }
}
