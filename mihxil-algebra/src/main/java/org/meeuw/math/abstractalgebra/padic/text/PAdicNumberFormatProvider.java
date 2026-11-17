package org.meeuw.math.abstractalgebra.padic.text;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.padic.PAdicNumber;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @since 0.24
 */
public class PAdicNumberFormatProvider extends AlgebraicElementFormatProvider<PAdicNumberFormat> {

    @Override
    public PAdicNumberFormat getInstance(Configuration configuration) {
        PAdicConfiguration conf = configuration.getAspect(PAdicConfiguration.class);
        return new PAdicNumberFormat(conf.getNotation(), conf.getMaximalNumberOfDigits());
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        return PAdicNumber.class.isAssignableFrom(element) ? 1 : -1;
    }
}
