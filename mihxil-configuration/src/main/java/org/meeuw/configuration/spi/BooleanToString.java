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
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof Boolean)
            .map(Object::toString);
    }

    @Override
    public Optional<Boolean> fromString(Class<?> type, @Nullable String value) {
        if (Boolean.TYPE.equals(type)) {
            type = Boolean.class;
        }
        final Class<?> finalType = type;
        return Optional.ofNullable(value)
            .filter(v -> Boolean.class.isAssignableFrom(finalType))
            .map(v -> {
                String lowered = value.toLowerCase();
                if ("true".equals(lowered)) {
                    return Boolean.TRUE;
                }
                if ("false".equals(lowered)) {
                    return Boolean.FALSE;
                }
                return null;
        });
    }
}
