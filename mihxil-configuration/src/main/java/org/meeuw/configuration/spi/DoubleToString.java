package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

@Log
public class DoubleToString extends NumberToString<Double> {

    @Override
    protected Double valueOf(String value) {
        return Double.valueOf(value);
    }
}
