package org.meeuw.configuration.spi;

import lombok.Getter;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.lang.System.Logger.Level.WARNING;

public abstract class AbstractToString<N> implements ToStringProvider<N> {

    System.Logger log = System.getLogger(AbstractToString.class.getName());

    @Getter
    protected final Class<?> type;

    @Override
    public int weight() {
        return 0;
    }

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
                } catch(RuntimeException iae) {
                    log.log(WARNING, value + "->" + t + ":" + iae.getClass().getName() + ":" + iae.getMessage());
                    return null;
                }
            });
    }

    protected  Class<?> toWrapper(Class<?> clazz) {
        return clazz;
    }
}
