import org.meeuw.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.configuration {
    requires static lombok;
    requires static org.checkerframework.checker.qual;
    requires java.logging;

    exports org.meeuw.configuration;

    uses ConfigurationAspect;
}

