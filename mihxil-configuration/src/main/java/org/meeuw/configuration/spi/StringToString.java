package org.meeuw.configuration.spi;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

public class StringToString implements ToStringProvider<CharSequence> {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof CharSequence)
            .map(Object::toString);
    }

    @Override
    public Optional<CharSequence> fromString(Class<?> type, @Nullable String value) {
        return Optional.<CharSequence>ofNullable(value)
            .filter(v -> CharSequence.class.isAssignableFrom(type));
    }
}
