package org.meeuw.math.uncertainnumbers;

import org.meeuw.math.abstractalgebra.FieldElement;

/**
 * An uncertain number as an element of {@link UncertainDoubleField}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainDoubleElement extends UncertainDouble, FieldElement<UncertainDoubleElement>{

    @Override
    default UncertainDoubleElement pow(int exponent) {
        return new ImmutableUncertainDouble(Math.pow(doubleValue(), exponent),
            Math.abs(exponent) * Math.pow(doubleValue(), exponent -1) * getUncertainty());
    }
}
