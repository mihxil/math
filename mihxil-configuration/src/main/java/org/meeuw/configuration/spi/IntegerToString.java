package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

@Log
public class IntegerToString extends NumberToString<Integer> {

    public IntegerToString() {
        super(Integer.TYPE, Integer.class);
    }

    @Override
    protected Integer valueOf(String value) {
        return Integer.valueOf(value);
    }
}
