package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

@Log
public class LongToString extends NumberToString<Long> {

    public LongToString() {
        super(Long.TYPE, Long.class);
    }

    @Override
    protected Long valueOf(String value) {
        return Long.valueOf(value);
    }
}
