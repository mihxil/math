package org.meeuw.configuration.spi;

import lombok.extern.java.Log;

@Log
public class FloatToString extends NumberToString<Float> {

    @Override
    protected Float valueOf(String value) {
        return Float.valueOf(value);
    }
}
