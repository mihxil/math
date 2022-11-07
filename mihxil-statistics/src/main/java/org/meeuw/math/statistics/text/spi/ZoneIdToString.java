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

import java.time.ZoneId;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.ToStringProvider;

public class ZoneIdToString implements ToStringProvider<ZoneId> {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof ZoneId)
            .map(Object::toString);
    }

    @Override
    public Optional<ZoneId> fromString(Class<?> type, @Nullable String value) {
        try {
            return Optional.ofNullable(value)
                .filter(v -> ZoneId.class.isAssignableFrom(type))
                .map(ZoneId::of);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
