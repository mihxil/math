package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

@Log
public class BooleanToString extends AbstractToString<Boolean> {

    public BooleanToString() {
        super(Boolean.class);
    }

    @Override
    public int weight() {
        return 0;
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof Boolean)
            .map(Object::toString);
    }

    @Override
    protected Boolean valueOf(String value) {
        String lowered = value.toLowerCase();
        if ("true".equals(lowered)) {
            return Boolean.TRUE;
        }
        if ("false".equals(lowered)) {
            return Boolean.FALSE;
        }
        return null;
    }

    @Override
    protected Class<?> toWrapper(Class<?> clazz) {
        if (Boolean.TYPE.equals(clazz)) {
            return type;
        }
        return clazz;
    }
}
