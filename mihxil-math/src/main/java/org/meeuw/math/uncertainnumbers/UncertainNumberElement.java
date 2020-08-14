package org.meeuw.math.uncertainnumbers;

import org.meeuw.math.abstractalgebra.FieldElement;

/**
 * An uncertain number as an element of {@link UncertainNumbersField}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainNumberElement extends UncertainNumber, FieldElement<UncertainNumberElement> {

    @Override
    default UncertainNumberElement pow(int exponent) {
        return new ImmutableUncertainNumber(Math.pow(doubleValue(), exponent),
            Math.abs(exponent) * Math.pow(doubleValue(), exponent -1) * getUncertainty());
    }
}
