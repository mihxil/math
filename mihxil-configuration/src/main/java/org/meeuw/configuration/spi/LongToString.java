package org.meeuw.configuration.spi;

public class LongToString extends NumberToString<Long> {

    public LongToString() {
        super(Long.TYPE, Long.class);
    }

    @Override
    protected Long valueOf(String value) {
        return Long.valueOf(value);
    }
}
