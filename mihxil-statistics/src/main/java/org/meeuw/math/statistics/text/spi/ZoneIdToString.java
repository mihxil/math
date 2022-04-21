package org.meeuw.math.statistics.text.spi;

import java.time.ZoneId;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.ToStringProvider;

public class ZoneIdToString implements ToStringProvider {
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
    public Optional<Object> fromString(Class<?> type, @Nullable String value) {
        try {
            return Optional.ofNullable(value)
                .filter(v -> ZoneId.class.isAssignableFrom(type))
                .map(ZoneId::of);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
