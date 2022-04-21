package org.meeuw.configuration.spi;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("unchecked")
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
                    return null;
                }
        });
    }
}
