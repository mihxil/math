import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.RandomConfiguration;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math {
    requires static lombok;
    requires static java.validation;
    requires static org.checkerframework.checker.qual;

    requires static ch.obermuhlner.math.big;
    requires java.logging;
    requires org.meeuw.configuration;

    exports org.meeuw.math;
    exports org.meeuw.math.abstractalgebra;
    exports org.meeuw.math.text;
    exports org.meeuw.math.uncertainnumbers;
    exports org.meeuw.math.uncertainnumbers.field;
    exports org.meeuw.math.exceptions;

    exports org.meeuw.math.text.spi;
    exports org.meeuw.math.text.configuration;
    exports org.meeuw.math.numbers;
    exports org.meeuw.math.streams;

    uses AlgebraicElementFormatProvider;
    uses ConfigurationAspect;

    provides AlgebraicElementFormatProvider with
        UncertainDoubleFormatProvider;
    provides ConfigurationAspect with
        StreamUtils.Configuration,
        NumberConfiguration,
        UncertaintyConfiguration,
        MathContextConfiguration,
        RandomConfiguration
        ;


}

