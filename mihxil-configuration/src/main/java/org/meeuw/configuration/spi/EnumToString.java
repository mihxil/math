package org.meeuw.configuration.spi;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.lang.System.Logger.Level.WARNING;

@SuppressWarnings("unchecked")
public class EnumToString implements ToStringProvider<Enum<?>> {

    private static final System.Logger log = System.getLogger(EnumToString.class.getName());


    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof Enum)
            .map(v -> ((Enum) v).name());
    }

    @Override
    public Optional<Enum<?>> fromString(Class<?> type, @Nullable String value) {
        return Optional.ofNullable(value)
            .filter(v -> type.isEnum())
            .map(v -> {
                try {
                    return Enum.valueOf((Class) type, value);
                } catch(IllegalArgumentException iae) {
                    log.log(WARNING, value + "->" + type + ":" + iae.getMessage());
                    return null;
                }
        });
    }
}
