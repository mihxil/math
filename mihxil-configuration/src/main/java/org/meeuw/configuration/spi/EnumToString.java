package org.meeuw.configuration.spi;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class EnumToString implements ToStringProvider {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(Object value) {
        return Optional.of(value)
            .filter(v -> v instanceof Enum)
            .map(v -> ((Enum) v).name());
    }

    @Override
    public Optional<Object> fromString(Class<?> type, String value) {
        return Optional.of(value)
            .filter(v -> type.isEnum())
            .map(v -> Enum.valueOf((Class) type, value));
    }
}
