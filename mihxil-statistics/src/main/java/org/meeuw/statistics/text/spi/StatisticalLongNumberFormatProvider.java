package org.meeuw.statistics.text.spi;

import java.time.ZoneId;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.Configuration;
import org.meeuw.statistics.StatisticalLong;
import org.meeuw.statistics.text.StatisticalLongNumberFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class StatisticalLongNumberFormatProvider extends AlgebraicElementFormatProvider {

    public static final String ZONE_ID = "ZONE_ID";

    @Override
    public StatisticalLongNumberFormat getInstance(Configuration configuration) {
        StatisticalLongNumberFormat format = new StatisticalLongNumberFormat();
        format.setZoneId(configuration.getPropertyOrDefault(ZONE_ID, ZoneId.systemDefault()));
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
}
