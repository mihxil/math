import org.meeuw.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.configuration {
    requires static lombok;
    requires static org.checkerframework.checker.qual;

    requires java.logging;
    requires java.prefs;

    exports org.meeuw.configuration;
    exports org.meeuw.configuration.spi;

    uses ConfigurationAspect;
    uses org.meeuw.configuration.spi.ToStringProvider;
}

