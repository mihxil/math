package org.meeuw.statistics.text.spi;

import java.util.Arrays;
import java.util.List;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.configuration.Configuration;
import org.meeuw.math.text.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.statistics.StatisticalLong;
import org.meeuw.statistics.text.StatisticalLongNumberFormat;
import org.meeuw.statistics.text.TimeConfiguration;

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
    public int weight(AlgebraicElement<?> element) {
        if (element instanceof StatisticalLong) {
            StatisticalLong e = (StatisticalLong) element;
            return e.getMode() != StatisticalLong.Mode.LONG ? 10 : 0;
        }
        return -1;

    }

    @Override
    public List<Class<? extends ConfigurationAspect>> getConfigurationAspects() {
        return Arrays.asList(TimeConfiguration.class);
    }
}
