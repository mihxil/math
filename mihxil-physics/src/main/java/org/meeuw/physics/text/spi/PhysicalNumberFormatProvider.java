package org.meeuw.physics.text.spi;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.physics.PhysicalNumber;
import org.meeuw.physics.text.PhysicalNumberFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class PhysicalNumberFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public PhysicalNumberFormat getInstance(Configuration configuration) {
        PhysicalNumberFormat format = new PhysicalNumberFormat();
        return format;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        if (PhysicalNumber.class.isAssignableFrom(element)) {
            return 100;
        }
        return -1;
    }
}
