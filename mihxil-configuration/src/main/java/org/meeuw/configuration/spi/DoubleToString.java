package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

@Log
public class DoubleToString extends NumberToString<Double> {

    public DoubleToString() {
        super(Double.TYPE, Double.class);
    }

    @Override
    protected Double valueOf(String value) {
        return Double.valueOf(value);
    }
}
