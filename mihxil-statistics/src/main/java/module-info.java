import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.spi.ToStringProvider;
import org.meeuw.math.statistics.text.TimeConfiguration;
import org.meeuw.math.statistics.text.spi.StatisticalLongNumberFormatProvider;
import org.meeuw.math.statistics.text.spi.ZoneIdToString;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.statistics {
    requires static lombok;
    requires static org.checkerframework.checker.qual;
    requires static jakarta.validation;
    requires static jakarta.annotation;
    requires static ch.obermuhlner.math.big;

    requires java.logging;
    requires org.meeuw.math;
    requires org.meeuw.configuration;

    exports org.meeuw.math.statistics;
    exports org.meeuw.math.statistics.text;
    exports org.meeuw.math.windowed;

    uses AlgebraicElementFormatProvider;
    uses ConfigurationAspect;


    provides AlgebraicElementFormatProvider with
        StatisticalLongNumberFormatProvider;

    provides ConfigurationAspect with
        TimeConfiguration;

    provides ToStringProvider with
        ZoneIdToString;


}

