package org.meeuw.math.numbers;

import jakarta.validation.constraints.Null;

import java.math.MathContext;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.ToStringProvider;

public class MathContextToString implements ToStringProvider {
    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof MathContext)
            .map(Object::toString);
    }

    @Override
    public Optional<Object> fromString(Class<?> type, @Null String value) {
        return Optional.ofNullable(value)
            .filter(v -> MathContext.class.isAssignableFrom(type))
            .map(MathContext::new);
    }
}
