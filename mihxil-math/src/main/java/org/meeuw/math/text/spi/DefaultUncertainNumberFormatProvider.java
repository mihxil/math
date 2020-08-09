package org.meeuw.math.text.spi;

import org.meeuw.math.uncertainnumbers.UncertainNumber;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.UncertainNumberFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DefaultUncertainNumberFormatProvider extends AlgebraicElementFormatProvider {
    @Override
    public UncertainNumberFormat getInstance(int minimumExponent) {
        UncertainNumberFormat format = new UncertainNumberFormat();
        format.setMinimumExponent(minimumExponent);
        return format;
    }

    @Override
    public int weight(AlgebraicElement<?> weight) {
        return weight instanceof UncertainNumber ? 1 : 0;
    }

}
