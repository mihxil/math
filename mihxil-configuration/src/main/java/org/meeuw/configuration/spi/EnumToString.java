package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("unchecked")
@Log
public class EnumToString implements ToStringProvider {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof Enum)
            .map(v -> ((Enum) v).name());
    }

    @Override
    public Optional<Object> fromString(Class<?> type, @Nullable String value) {
        return Optional.ofNullable(value)
            .filter(v -> type.isEnum())
            .map(v -> {
                try {
                    return Enum.valueOf((Class) type, value);
                } catch(IllegalArgumentException iae) {
                    log.warning(value + "->" + type + ":" + iae.getMessage());
                    return null;
                }
        });
    }
}
