package org.meeuw.math.text.spi;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.text.configuration.*;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.math.text.spi.FormatService.getConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public UncertainDoubleFormat getInstance(Configuration configuration) {
        UncertainDoubleFormat format = new UncertainDoubleFormat();
        format.setMinimumExponent(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent());
        format.setUncertaintyNotation(getConfigurationAspect(UncertaintyConfiguration.class).getNotation());
        format.setConsiderRoundingErrorFactor(getConfigurationAspect(UncertaintyConfiguration.class).getConsiderRoundingErrorFactor());
        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        return UncertainDouble.class.isAssignableFrom(element) ? 1 : 0;
    }

    @Override
    public List<Class<? extends ConfigurationAspect>> getConfigurationAspects() {
        return Arrays.asList(NumberConfiguration.class, UncertaintyConfiguration.class);
    }

}
