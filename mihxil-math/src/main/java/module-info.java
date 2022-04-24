import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.spi.ToStringProvider;
import org.meeuw.math.abstractalgebra.GenericGroupConfiguration;
import org.meeuw.math.abstractalgebra.RandomConfiguration;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.numbers.MathContextToString;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;
import org.meeuw.math.uncertainnumbers.ConfidenceIntervalConfiguration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math {
    requires static lombok;
    requires static org.checkerframework.checker.qual;
    requires static ch.obermuhlner.math.big;

    requires java.logging;
    requires jakarta.validation;
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
    exports org.meeuw.math.operators;
    exports org.meeuw.math.validation;
    exports org.meeuw.math.abstractalgebra.product;
    exports org.meeuw.math.abstractalgebra.trivial;
    exports org.meeuw.math.abstractalgebra.categoryofgroups;

    uses AlgebraicElementFormatProvider;
    uses ConfigurationAspect;

    provides AlgebraicElementFormatProvider with
        UncertainDoubleFormatProvider;
    provides ConfigurationAspect with
        StreamUtils.Configuration,
        NumberConfiguration,
        UncertaintyConfiguration,
        MathContextConfiguration,
        RandomConfiguration,
        GenericGroupConfiguration,
        ConfidenceIntervalConfiguration
        ;
    provides ToStringProvider with MathContextToString;


}

