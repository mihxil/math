package org.meeuw.statistics.text.spi;

import org.meeuw.math.UncertainNumber;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.statistics.text.StatisticalLongNumberFormat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class StatisticalLongNumberFormatProvider extends AlgebraicElementFormatProvider {
    @Override
    public StatisticalLongNumberFormat getInstance(int minimumExponent) {
        StatisticalLongNumberFormat format = new StatisticalLongNumberFormat();
        return format;

    }

    @Override
    public int weight(UncertainNumber<?> weight) {
        return weight instanceof StatisticalLongNumberFormat ? 1 : -1;

    }
}
