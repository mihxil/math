package org.meeuw.math.text.spi;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public UncertainDoubleFormat getInstance(Configuration configuration) {
        UncertainDoubleFormat format = new UncertainDoubleFormat();
        format.setMinimumExponent(configuration.getMinimalExponent());
        format.setUncertaintyNotation(configuration.getUncertaintyNotation());
        return format;
    }

    @Override
    public int weight(AlgebraicElement<?> weight) {
        return weight instanceof UncertainDouble ? 1 : 0;
    }

}
