package org.meeuw.math.text.spi;

import java.util.Arrays;
import java.util.List;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.text.configuration.*;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public UncertainDoubleFormat getInstance(ConfigurationService configuration) {
        UncertainDoubleFormat format = new UncertainDoubleFormat();
        format.setMinimumExponent(ConfigurationService.getConfiguration(NumberConfiguration.class).getMinimalExponent());
        format.setUncertaintyNotation(ConfigurationService.getConfiguration(UncertaintyConfiguration.class).getNotation());
        return format;
    }

    @Override
    public int weight(AlgebraicElement<?> element) {
        return element instanceof UncertainDouble ? 1 : 0;
    }

    @Override
    public List<Configuration> getConfigurationSettings() {
        return Arrays.asList(
            new NumberConfiguration(4),
            new UncertaintyConfiguration(UncertaintyConfiguration.Notation.PLUS_MINUS)
        );
    }

}
