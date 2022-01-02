package org.meeuw.physics.text.spi;

import org.meeuw.configuration.Configuration;
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
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        if (DimensionalAnalysis.class.isAssignableFrom(element)) {
            return 10;
        }
        return -1;
    }
}
