package org.meeuw.math.temporal;

import java.util.function.*;

import org.meeuw.math.statistics.StatisticalNumber;

/**
 *
 */
public interface StatisticalTemporal<SELF extends StatisticalTemporal<SELF, N>, N extends Number>
    extends
    UncertainTemporal<N>, StatisticalNumber<SELF, N>, LongConsumer, IntConsumer {

}
