package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.List;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.configuration.Configuration;
import org.meeuw.math.text.configuration.ConfigurationService;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AlgebraicElementFormatProvider {

    public abstract Format getInstance(ConfigurationService configuration);

    public abstract int weight(AlgebraicElement<?> weight);

    public abstract List<Configuration> getConfigurationSettings();

}
