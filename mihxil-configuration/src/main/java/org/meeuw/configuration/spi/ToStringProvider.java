package org.meeuw.configuration.spi;

import java.util.Optional;

public interface ToStringProvider extends Comparable<ToStringProvider> {

    int weight();

    Optional<String> toString(Object value);

    Optional<Object> fromString(Class<?> type, String value);

    @Override
    default int compareTo(ToStringProvider other) {
        return weight() - other.weight();
    }
}
