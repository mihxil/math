package org.meeuw.math.numbers;

import java.math.MathContext;
import java.util.Optional;

import org.meeuw.configuration.spi.ToStringProvider;

public class MathContextToString implements ToStringProvider {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(Object value) {
        return Optional.of(value)
            .filter(v -> v instanceof MathContext)
            .map(Object::toString);
    }

    @Override
    public Optional<Object> fromString(Class<?> type, String value) {
        return Optional.of(value)
            .filter(v -> MathContext.class.isAssignableFrom(type))
            .map(MathContext::new);
    }
}
