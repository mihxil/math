package org.meeuw.configuration.spi;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ToStringProvider extends Comparable<ToStringProvider> {

    int weight();

    Optional<String> toString(@Nullable Object value);

    Optional<Object> fromString(Class<?> type, @Nullable String value);

    @Override
    default int compareTo(ToStringProvider other) {
        return weight() - other.weight();
    }
}
