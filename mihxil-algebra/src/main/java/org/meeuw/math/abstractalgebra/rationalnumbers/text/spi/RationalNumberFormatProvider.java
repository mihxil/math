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
package org.meeuw.math.abstractalgebra.rationalnumbers.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberConfiguration;
import org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberFormat;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.20
 */
public class RationalNumberFormatProvider extends AlgebraicElementFormatProvider<RationalNumberFormat> {

    @Override
    public RationalNumberFormat getInstance(Configuration configuration) {
        RationalNumberConfiguration configurationAspect = getConfigurationAspect(RationalNumberConfiguration.class);
        return new RationalNumberFormat(configurationAspect.getMode());
    }

    /**
     * A bit heavier then {@link org.meeuw.math.text.UncertainNumberFormat} for {@link UncertainDouble}.
     */
    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        return RationalNumberFormat.class.isAssignableFrom(element) ? 2 : -1;
    }
}
