import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.statistics.text.TimeConfiguration;
import org.meeuw.math.statistics.text.spi.StatisticalLongNumberFormatProvider;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.statistics {
    requires static lombok;
    requires java.logging;
    requires org.meeuw.math;
    requires jakarta.validation;
    requires org.meeuw.configuration;

    exports org.meeuw.math.statistics;
    exports org.meeuw.math.statistics.text;


    uses AlgebraicElementFormatProvider;
    uses ConfigurationAspect;


    provides AlgebraicElementFormatProvider with
        StatisticalLongNumberFormatProvider;

    provides ConfigurationAspect with
        TimeConfiguration;


}

