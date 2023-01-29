package org.meeuw.configuration.spi;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@Log
public abstract class AbstractToString<N extends Number> implements ToStringProvider<N> {

    @Getter
    protected final Class<?> type;

    protected AbstractToString(Class<N> type) {
        this.type = type;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(type::isInstance)
            .map(Object::toString);
    }

    protected abstract N valueOf(String value);

    @Override
    public Optional<N> fromString(Class<?> t, @Nullable String value) {
        Class<?> finalType = toWrapper(t);
        return Optional.ofNullable(value)
            .filter(v -> type.isAssignableFrom(finalType))
            .map(v -> {
                try {
                    return valueOf(value);
                } catch(IllegalArgumentException iae) {
                    log.warning(value + "->" + t + ":" + iae.getMessage());
                    return null;
                }
            });
    }

    protected Class<?> toWrapper(Class<?> clazz) {
        return clazz;
    }
}
