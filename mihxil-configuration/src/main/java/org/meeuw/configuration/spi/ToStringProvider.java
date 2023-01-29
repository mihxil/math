package org.meeuw.configuration.spi;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * SPI to serialize and deserialize to and from String, without resorting to serialization.
 * <p>
 * E.g. a {@link java.math.MathContext} can be serialized, but can also be written to String and parsed by very naturally.
 * <p>
 * This can only be used if the type is known beforehand.
 */
public interface ToStringProvider<C> extends Comparable<ToStringProvider<?>> {

    int weight();

    /**
     * For given value, check if the type is supported, and then convert it to a String.
     *
     */
    Optional<String> toString(@Nullable Object value);

    /**
     * Given a desired type, and a string value, convert it to the supported type.
     * Normally, the given type must be some superclass of <code>C</code>, but it also might be a primitive type.
     */
    Optional<C> fromString(Class<?> type, @Nullable String value);

    @Override
    default int compareTo(ToStringProvider other) {
        return weight() - other.weight();
    }
}
