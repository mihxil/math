package org.meeuw.test.math.text.spi.test;

import java.text.Format;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 */
public class TestFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public Format getInstance(Configuration configuration) {
        return null;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> weight) {
        return 0;
    }

}
