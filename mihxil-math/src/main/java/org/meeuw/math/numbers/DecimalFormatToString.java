package org.meeuw.math.numbers;

import java.text.DecimalFormat;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.ToStringProvider;

public class DecimalFormatToString implements ToStringProvider<DecimalFormat> {

    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof DecimalFormat)
            .map(o -> ((DecimalFormat)o ).toPattern());
    }

    @Override
    public Optional<DecimalFormat> fromString(Class<?> type, @Nullable String value) {
        try {
            return Optional.ofNullable(value)
                .filter(v -> DecimalFormat.class.isAssignableFrom(type))
                .map(s -> new DecimalFormat());
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
