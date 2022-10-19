package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@Log
public class BooleanToString implements ToStringProvider<Boolean> {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<Boolean> fromString(Class<?> type, @Nullable String value) {
        return Optional.ofNullable(value)
            .filter(v -> Boolean.class.isAssignableFrom(type))
            .map(v -> {
                try {
                    return Boolean.valueOf(value);
                } catch(IllegalArgumentException iae) {
                    log.warning(value + "->" + type + ":" + iae.getMessage());
                    return null;
                }
        });
    }
}
