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
package org.meeuw.math.statistics.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.temporal.UncertainTemporal;
import org.meeuw.math.statistics.text.UncertainTemporalFormat;
import org.meeuw.math.statistics.text.TimeConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainTemporalFormatProvider extends AlgebraicElementFormatProvider<UncertainTemporalFormat> {

    @Override
    public UncertainTemporalFormat getInstance(Configuration configuration) {
        UncertainTemporalFormat format = new UncertainTemporalFormat();
        format.setZoneId(configuration.getAspect(TimeConfiguration.class).getZoneId());
        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        if (UncertainTemporal.class.isAssignableFrom(element)) {
            return 10;
        }
        return -1;
    }

}
