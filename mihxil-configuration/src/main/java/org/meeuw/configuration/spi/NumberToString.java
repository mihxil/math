package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

@Log
public abstract class NumberToString<N extends Number> extends AbstractToString<N> {

    private final Class<?> primitiveType;

    protected NumberToString(Class<?> primitiveType, Class<N> boxedType) {
        super(boxedType);
        this.primitiveType = primitiveType;
    }

    @Override
    public int weight() {
        return 0;
    }

    @Override
    protected Class<?> toWrapper(Class<?> clazz) {
        if (clazz == primitiveType) {
            return type;
        }
        return clazz;
    }
}
