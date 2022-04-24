import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.test.configuration.ConfigurationServiceTest;
import org.meeuw.test.configuration.spi.TestConfigurationAspect;
import org.meeuw.test.configuration.spi.InvalidConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.test.configuration {
    uses ConfigurationAspect;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive org.apache.logging.log4j;
    requires static lombok;
    requires java.logging;
    requires java.prefs;
    requires org.meeuw.configuration;

    opens org.meeuw.test.configuration;

    exports org.meeuw.test.configuration;
    exports org.meeuw.test.configuration.spi;


    provides ConfigurationAspect with
        ConfigurationServiceTest.TestConfiguration,
        InvalidConfigurationAspect,
        TestConfigurationAspect;

}

