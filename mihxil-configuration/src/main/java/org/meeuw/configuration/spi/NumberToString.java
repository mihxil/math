package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@Log
public abstract class NumberToString<N extends Number> implements ToStringProvider<N> {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof Number)
            .map(Object::toString);
    }

    protected abstract N valueOf(String value);

    @Override
    public Optional<N> fromString(Class<?> type, @Nullable String value) {
        return Optional.ofNullable(value)
            .filter(v -> type.isEnum())
            .map(v -> {
                try {
                    return valueOf(value);
                } catch(IllegalArgumentException iae) {
                    log.warning(value + "->" + type + ":" + iae.getMessage());
                    return null;
                }
        });
    }
}
