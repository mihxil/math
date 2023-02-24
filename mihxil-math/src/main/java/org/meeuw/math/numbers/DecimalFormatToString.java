package org.meeuw.math.numbers;

import java.text.DecimalFormat;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.AbstractToString;

public class DecimalFormatToString extends AbstractToString<DecimalFormat> {

    public DecimalFormatToString() {
        super(DecimalFormat.class);
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof DecimalFormat)
            .map(o -> ((DecimalFormat)o ).toPattern());
    }

    @Override
    protected DecimalFormat valueOf(String value) {
        return new DecimalFormat(value);
    }


}
