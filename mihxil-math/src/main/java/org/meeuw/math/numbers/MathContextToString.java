package org.meeuw.math.numbers;

import java.math.MathContext;

import org.meeuw.configuration.spi.AbstractToString;

public class MathContextToString extends AbstractToString<MathContext> {


    public MathContextToString() {
        super(MathContext.class);
    }

    @Override
    protected MathContext valueOf(String value) {
        return new MathContext(value);
    }

}
