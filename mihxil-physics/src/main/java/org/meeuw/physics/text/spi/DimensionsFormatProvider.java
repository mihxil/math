package org.meeuw.physics.text.spi;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.physics.DimensionalAnalysis;
import org.meeuw.physics.text.DimensionalAnalysisFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class DimensionsFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public DimensionalAnalysisFormat getInstance(Configuration configuration) {
        DimensionalAnalysisFormat format = new DimensionalAnalysisFormat();
        return format;
    }

    @Override
    public int weight(AlgebraicElement<?> element) {
        if (element instanceof DimensionalAnalysis) {
            return 10;
        }
        return -1;
    }

    @Override
    public List<Class<? extends ConfigurationAspect>> getConfigurationAspects() {
        return Arrays.asList();
    }
}
