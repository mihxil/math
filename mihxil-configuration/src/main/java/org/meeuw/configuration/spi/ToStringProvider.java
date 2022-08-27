package org.meeuw.configuration.spi;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * SPI to serialize and deserialize to and from String, without resorting to serialization.
 *
 * E.g. a {@link java.math.MathContext} can be serialized, but can also be written to String and parsed by very naturally.
 *
 * This can only be used if the type is known beforehand.
 */
public interface ToStringProvider<C> extends Comparable<ToStringProvider<?>> {

    int weight();

    default Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value).map(v -> String.valueOf(value));
    }

    Optional<C> fromString(Class<?> type, @Nullable String value);

    @Override
    default int compareTo(ToStringProvider other) {
        return weight() - other.weight();
    }
}
