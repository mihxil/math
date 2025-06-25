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
package org.meeuw.time.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.time.text.TimeConfiguration;
import org.meeuw.time.text.UncertainTimeFormat;
import org.meeuw.time.UncertainJavaTime;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.time.UncertainDuration;
import org.meeuw.time.UncertainInstant;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainTemporalFormatProvider extends AlgebraicElementFormatProvider<UncertainTimeFormat> {

    @Override
    public UncertainTimeFormat getInstance(Configuration configuration) {
        UncertainTimeFormat format = new UncertainTimeFormat();
        format.setZoneId(configuration.getAspect(TimeConfiguration.class).getZoneId());
        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        if (UncertainJavaTime.class.isAssignableFrom(element)) {
            return 10;
        }
        if (UncertainInstant.class.isAssignableFrom(element)) {
            return 10;
        }
        if (UncertainDuration.class.isAssignableFrom(element)) {
            return 10;
        }
        return -1;
    }

}
