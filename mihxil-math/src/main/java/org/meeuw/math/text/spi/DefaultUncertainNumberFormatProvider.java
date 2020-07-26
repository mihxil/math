package org.meeuw.math.text.spi;

import org.meeuw.math.UncertainNumber;
import org.meeuw.math.text.UncertainNumberFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DefaultUncertainNumberFormatProvider extends UncertainNumberFormatProvider {
    @Override
    public UncertainNumberFormat getInstance(int minimumExponent) {
        UncertainNumberFormat format = new UncertainNumberFormat();
        format.setMinimumExponent(minimumExponent);
        return format;
    }

    @Override
    public int weight(UncertainNumber<?> weight) {
        return 0;
    }
}
