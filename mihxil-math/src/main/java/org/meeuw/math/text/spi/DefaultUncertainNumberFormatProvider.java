package org.meeuw.math.text.spi;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.UncertainNumberFormat;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DefaultUncertainNumberFormatProvider extends AlgebraicElementFormatProvider {

    public static final DefaultUncertainNumberFormatProvider INSTANCE = new DefaultUncertainNumberFormatProvider();

    @Override
    public UncertainNumberFormat getInstance(int minimumExponent) {
        UncertainNumberFormat format = new UncertainNumberFormat();
        format.setMinimumExponent(minimumExponent);
        return format;
    }

    @Override
    public int weight(AlgebraicElement<?> weight) {
        return weight instanceof UncertainDouble ? 1 : 0;
    }

}
