package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.Format;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public Format getInstance(int minimumExponent) {
        return null;
    }

    @Override
    public int weight(AlgebraicElement<?> weight) {
        return 0;
    }
}
