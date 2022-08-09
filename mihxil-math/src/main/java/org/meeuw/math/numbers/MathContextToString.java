package org.meeuw.math.numbers;

import java.math.MathContext;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.ToStringProvider;

public class MathContextToString implements ToStringProvider<MathContext> {
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
    public Optional<MathContext> fromString(Class<?> type, @Nullable String value) {
        try {
            return Optional.ofNullable(value)
                .filter(v -> MathContext.class.isAssignableFrom(type))
                .map(MathContext::new);
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
