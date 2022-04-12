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
package org.meeuw.math.statistics.text;

import lombok.Getter;
import lombok.With;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * Contains time related settings.
 * E.g. to format an instance as a {@link java.time.LocalDateTime} the preferred {@link ZoneId} is relevant.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TimeConfiguration implements ConfigurationAspect {

    /**
     * The zone id used for representing instances when that is relevant.
     */
    @Getter
    @With
    final ZoneId zoneId;

    @lombok.Builder
    private TimeConfiguration(ZoneId zoneId) {
        this.zoneId = zoneId;
    }
    public TimeConfiguration() {
        this(ZoneId.systemDefault());
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(StatisticalLongNumberFormat.class);
    }
}
