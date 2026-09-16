package org.meeuw.math.numbers;

import java.text.DecimalFormat;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.spi.AbstractToString;

public class DecimalFormatToString extends AbstractToString<DecimalFormat> {

    private static final int MAXIMUM_PATTERN_FRACTION_DIGITS = 10_000;

    public DecimalFormatToString() {
        super(DecimalFormat.class);
    }

    @Override
    public Optional<String> toString(@Nullable Object value) {
        return Optional.ofNullable(value)
            .filter(v -> v instanceof DecimalFormat)
            .map(DecimalFormat.class::cast)
            .filter(format -> format.getMaximumFractionDigits() <= MAXIMUM_PATTERN_FRACTION_DIGITS)
            .map(DecimalFormat::toPattern);
    }

    @Override
    protected DecimalFormat valueOf(String value) {
        return new DecimalFormat(value);
    }


}
