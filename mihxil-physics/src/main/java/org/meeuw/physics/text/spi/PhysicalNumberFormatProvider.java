package org.meeuw.physics.text.spi;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
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
    public int weight(AlgebraicElement<?> element) {
        if (element instanceof PhysicalNumber) {
            return 100;
        }
        return -1;
    }

    @Override
    public List<Class<? extends ConfigurationAspect>> getConfigurationAspects() {
        return Arrays.asList();
    }
}
