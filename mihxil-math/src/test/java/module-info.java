import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.test.math.text.spi.test.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
open module org.meeuw.test {

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive org.apache.logging.log4j;

    requires org.meeuw.math;
    requires lombok;
    requires org.meeuw.configuration;
    requires org.checkerframework.checker.qual;
    requires jakarta.validation;
    requires org.hibernate.validator;

    exports org.meeuw.test.math.text.spi.test;


    uses ConfigurationAspect;
    uses AlgebraicElementFormatProvider;

    provides ConfigurationAspect with
        InvalidConfigurationAspect,
        TestConfigurationAspect;

    provides AlgebraicElementFormatProvider with
        TestFormatProvider;

}

