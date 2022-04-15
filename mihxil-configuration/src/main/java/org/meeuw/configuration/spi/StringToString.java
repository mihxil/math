package org.meeuw.configuration.spi;

import java.util.Optional;

public class StringToString implements ToStringProvider {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(Object value) {
        return Optional.of(value)
            .filter(v -> v instanceof CharSequence)
            .map(Object::toString);
    }

    @Override
    public Optional<Object> fromString(Class<?> type, String value) {
        return Optional.of(value);
    }
}
