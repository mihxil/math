package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@Log
public abstract class NumberToString<N extends Number> implements ToStringProvider<N> {

    private final Class<?> primitiveType;
    private final Class<?> boxedType;

    protected NumberToString(Class<?> primitiveType, Class<N> boxedtype) {
        this.primitiveType = primitiveType;
        this.boxedType = boxedtype;
    }

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
        Class<?> finalType = toWrapper(type);
        return Optional.ofNullable(value)
            .filter(v -> Number.class.isAssignableFrom(finalType))
            .map(v -> {
                try {
                    return valueOf(value);
                } catch(IllegalArgumentException iae) {
                    log.warning(value + "->" + type + ":" + iae.getMessage());
                    return null;
                }
        });
    }

    protected Class<?> toWrapper(Class<?> clazz) {
        if (clazz == primitiveType) {
            return boxedType;
        }
        return clazz;
    }
}
