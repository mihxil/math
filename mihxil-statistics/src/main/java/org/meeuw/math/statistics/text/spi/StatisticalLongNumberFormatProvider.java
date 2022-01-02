package org.meeuw.math.statistics.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.statistics.text.StatisticalLongNumberFormat;
import org.meeuw.math.statistics.text.TimeConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class StatisticalLongNumberFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public StatisticalLongNumberFormat getInstance(Configuration configuration) {
        StatisticalLongNumberFormat format = new StatisticalLongNumberFormat();
        format.setZoneId(configuration.getAspect(TimeConfiguration.class).getZoneId());
        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        if (StatisticalLong.class.isAssignableFrom(element)) {
            return 10;
        }
        return -1;
    }

}
