package org.meeuw.math.statistics.text.spi;

import java.util.*;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.statistics.text.StatisticalLongNumberFormat;
import org.meeuw.math.statistics.text.TimeConfiguration;

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
        return Collections.singletonList(TimeConfiguration.class);
    }
}
