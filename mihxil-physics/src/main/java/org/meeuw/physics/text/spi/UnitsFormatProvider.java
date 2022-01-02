package org.meeuw.physics.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.physics.Units;
import org.meeuw.physics.text.UnitsFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class UnitsFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public UnitsFormat getInstance(Configuration configuration) {
        return UnitsFormat.INSTANCE;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        if (Units.class.isAssignableFrom(element)) {
            return 10;
        }
        return -1;
    }
}
